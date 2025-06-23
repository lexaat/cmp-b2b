package core.usecase

import core.presentation.BaseSideEffect
import core.session.LogoutManager
import data.model.ApiResponse
import features.auth.domain.model.RefreshTokenRequest
import features.auth.domain.usecase.RefreshTokenUseCase
import features.common.domain.auth.TokenManager

class RefreshWrapper(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val logoutManager: LogoutManager
) {

    suspend fun <T> runWithRefresh(
        originalCall: suspend () -> ApiResponse<T>
    ): ResultWithEffect<T, BaseSideEffect> {
        val response = originalCall()

        if (response.error?.code == 61607) {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                logoutManager.clearSession()
                return ResultWithEffect(sideEffect = BaseSideEffect.SessionExpired)
            }

            val refreshResult = refreshTokenUseCase(RefreshTokenRequest(refreshToken = refreshToken))
            val refreshedAuth = refreshResult.result
            if (refreshedAuth != null) {
                tokenManager.saveTokens(refreshedAuth.accessToken, refreshedAuth.refreshToken)
                val retried = originalCall()
                return retried.error?.let {
                    ResultWithEffect(sideEffect = BaseSideEffect.ShowError(it.message))
                } ?: ResultWithEffect(result = retried.result)
            } else {
                logoutManager.clearSession()
                return ResultWithEffect(sideEffect = BaseSideEffect.SessionExpired)
            }
        }

        return response.error?.let {
            ResultWithEffect(sideEffect = BaseSideEffect.ShowError(it.message))
        } ?: ResultWithEffect(result = response.result)
    }
}
