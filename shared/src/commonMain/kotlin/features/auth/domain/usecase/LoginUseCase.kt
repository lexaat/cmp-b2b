package features.auth.domain.usecase

import core.presentation.BaseSideEffect
import features.auth.domain.AuthRepository
import features.auth.domain.model.AuthResult
import features.auth.domain.model.LoginRequest
import features.auth.presentation.login.LoginSideEffect
import kotlinx.io.IOException
import core.usecase.RefreshWrapper
import core.usecase.ResultWithEffect
import features.auth.presentation.otp.OtpSideEffect

class LoginUseCase(
    private val repository: AuthRepository,
    private val refreshWrapper: RefreshWrapper
) {
    suspend operator fun invoke(request: LoginRequest): ResultWithEffect<AuthResult, BaseSideEffect> {
        return try {
            val response = repository.login(request)

            response.error?.let { error ->
                when (error.code) {
                    61607 -> refreshWrapper.runWithRefresh { repository.login(request) }
                    61712 -> {
                        val phone = extractPhoneNumber(error.message)
                        ResultWithEffect(sideEffect = LoginSideEffect.NavigateToOtp(phone))
                    }
                    60150 -> ResultWithEffect(sideEffect = OtpSideEffect.NavigateToPasswordChange)
                    else -> ResultWithEffect(sideEffect = BaseSideEffect.ShowError(error.message))
                }
            } ?: ResultWithEffect(result = response.result)
        } catch (e: IOException) {
            ResultWithEffect(sideEffect = BaseSideEffect.ShowError("Нет подключения к интернету"))
        } catch (e: Exception) {
            ResultWithEffect(sideEffect = BaseSideEffect.ShowError("Неизвестная ошибка: ${e.message}"))
        }
    }

    private fun extractPhoneNumber(message: String): String {
        val regex = Regex("""на номер ([\d* ]+)\.""")
        return regex.find(message)?.groupValues?.getOrNull(1) ?: ""
    }
}
