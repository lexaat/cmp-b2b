package core.error

import core.presentation.BaseSideEffect
import core.session.LogoutManager
import data.model.ApiResponse
import features.auth.domain.model.RefreshTokenRequest
import features.auth.domain.usecase.RefreshTokenUseCase
import features.auth.presentation.login.AuthSideEffect
import features.auth.presentation.otp.OtpSideEffect
import features.common.domain.auth.TokenManager

/** Универсальный результат: результат или sideEffect (одноразовое событие) */
data class ResultWithEffect<T, S>(
    val result: T? = null,
    val sideEffect: S? = null
)

class ApiCallHandler(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val logoutManager: LogoutManager
) {

    /** Главная функция, используемая во всех фичах */
    suspend fun <T, SIDE_EFFECT : BaseSideEffect> handleApiCall(
        call: suspend () -> ApiResponse<T>,
        retry: suspend () -> ApiResponse<T>,
        effectMapper: (BaseSideEffect) -> SIDE_EFFECT // для маппинга глобальных эффектов на фичевые, если надо
    ): ResultWithEffect<T, SIDE_EFFECT> {
        val response = call()
        response.error?.let { error ->
            return when (error.code) {
                61607 -> { // Access token expired, пытаемся refresh
                    val refreshToken = tokenManager.getRefreshToken()
                    if (refreshToken.isNullOrBlank()) {
                        logoutManager.clearSession()
                        ResultWithEffect(sideEffect = effectMapper(BaseSideEffect.SessionExpired))
                    } else {
                        val refreshResponse = refreshTokenUseCase(RefreshTokenRequest(refresh_token = refreshToken))
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
                    ResultWithEffect(sideEffect = effectMapper(AuthSideEffect.NavigateToOtp))
                }
                60150 -> {
                    ResultWithEffect(sideEffect = effectMapper(OtpSideEffect.NavigateToPasswordChange))
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
