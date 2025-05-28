package core.error

import core.model.ResultWithEffect
import core.presentation.BaseSideEffect
import core.session.LogoutManager
import data.model.ApiResponse
import features.auth.domain.AuthRepository
import features.auth.presentation.login.AuthSideEffect
import features.common.domain.auth.TokenManager

class DefaultErrorHandler(
    private val authRepository: AuthRepository, // нужен метод refreshToken
    private val logoutManager: LogoutManager,
    private val tokenManager: TokenManager
) : ApiErrorHandler<BaseSideEffect> {

    override suspend fun <T> handleApiCall(
        call: suspend () -> ApiResponse<T>,
        retry: suspend () -> ApiResponse<T>
    ): ResultWithEffect<T, BaseSideEffect> {
        val response = call()
        response.error?.let { error ->
            return when (error.code) {
                61607 -> {
                    val refreshToken = tokenManager.getRefreshToken()
                    if (refreshToken.isNullOrBlank()) {
                        logoutManager.clearSession()
                        return ResultWithEffect(sideEffect = BaseSideEffect.SessionExpired)
                    }

                    val refreshResponse = authRepository.refreshToken(refreshToken = refreshToken)

                    if (refreshResponse.error?.code == 61608) {
                        logoutManager.clearSession()
                        ResultWithEffect(sideEffect = BaseSideEffect.SessionExpired)
                    } else {
                        val retried = retry()
                        retried.error?.let {
                            ResultWithEffect(sideEffect = BaseSideEffect.ShowError(it.message))
                        } ?: ResultWithEffect(result = retried.result)
                    }
                }

                61608 -> {
                    logoutManager.clearSession()
                    ResultWithEffect(sideEffect = BaseSideEffect.SessionExpired)
                }

                61712 -> {
                    ResultWithEffect(sideEffect = AuthSideEffect.NavigateToOtp)
                }

                else -> ResultWithEffect(sideEffect = BaseSideEffect.ShowError(error.message))
            }
        }

        return ResultWithEffect(result = response.result)
    }
}
