package features.auth.domain.usecase

import core.presentation.BaseSideEffect
import core.usecase.RefreshWrapper
import core.usecase.ResultWithEffect
import data.model.ApiResponse
import features.auth.domain.AuthRepository
import features.auth.domain.model.AuthResult
import features.auth.domain.model.ChangePasswordRequest
import kotlinx.io.IOException


class ChangePasswordUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        request: ChangePasswordRequest
    ): ResultWithEffect<ApiResponse<String>, BaseSideEffect> {
        return try {
            val response = repository.changePassword(request)

            response.error?.let { error ->
                return when (error.code) {
                    else -> ResultWithEffect(sideEffect = BaseSideEffect.ShowError(error.message))
                }
            }

            ResultWithEffect(result = response)

        } catch (e: IOException) {
            ResultWithEffect(sideEffect = BaseSideEffect.ShowError("Нет подключения к интернету"))
        } catch (e: Exception) {
            ResultWithEffect(sideEffect = BaseSideEffect.ShowError("Неизвестная ошибка: ${e.message}"))
        }
    }
}
