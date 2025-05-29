package core.error

import core.presentation.BaseSideEffect
import features.auth.domain.AuthRepository
import features.auth.presentation.login.AuthSideEffect
import features.common.domain.auth.TokenManager
import core.session.LogoutManager
import data.model.ApiResponse
import features.auth.presentation.AuthScreen
import features.main.presentation.MainScreen

/** Универсальный результат: результат или sideEffect (одноразовое событие) */
data class ResultWithEffect<T, S>(
    val result: T? = null,
    val sideEffect: S? = null
)

class ApiCallHandler(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val logoutManager: LogoutManager
) {

    /** Главная функция, используемая во всех фичах */
    suspend fun <T> handleApiCall(
        call: suspend () -> ApiResponse<T>,
        retry: suspend () -> ApiResponse<T>,
        effectMapper: (BaseSideEffect) -> AuthSideEffect // для маппинга глобальных эффектов на фичевые, если надо
    ): ResultWithEffect<T, AuthSideEffect> {
        val response = call()
        response.error?.let { error ->
            return when (error.code) {
                61607 -> { // Access token expired, пытаемся refresh
                    val refreshToken = tokenManager.getRefreshToken()
                    if (refreshToken.isNullOrBlank()) {
                        logoutManager.clearSession()
                        ResultWithEffect(sideEffect = effectMapper(BaseSideEffect.SessionExpired))
                    } else {
                        val refreshResponse = authRepository.refreshToken(refreshToken = refreshToken)
                        if (refreshResponse.error?.code == 61608) {
                            logoutManager.clearSession()
                            ResultWithEffect(sideEffect = effectMapper(BaseSideEffect.SessionExpired))
                        } else {
                            val responseResult = refreshResponse.result

                            if (responseResult != null) {
                                tokenManager.saveTokens(responseResult.accessToken, responseResult.refreshToken)
                            } else {
                                tokenManager.clearAccessToken()
                            }

                            val retried = retry()
                            retried.error?.let {
                                ResultWithEffect(sideEffect = effectMapper(BaseSideEffect.ShowError(it.message)))
                            } ?: ResultWithEffect(result = retried.result)
                        }
                    }
                }
                61712 -> {
                    ResultWithEffect(sideEffect = AuthSideEffect.NavigateToOtp)
                }
                61608 -> {
                    logoutManager.clearSession()
                    ResultWithEffect(sideEffect = effectMapper(BaseSideEffect.SessionExpired))
                }
                else -> ResultWithEffect(sideEffect = effectMapper(BaseSideEffect.ShowError(error.message)))
            }
        }
        // Если всё ок
        return ResultWithEffect(result = response.result)
    }
}
