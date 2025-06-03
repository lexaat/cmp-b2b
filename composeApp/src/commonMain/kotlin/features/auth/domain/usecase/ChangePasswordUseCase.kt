package features.auth.domain.usecase

import core.presentation.BaseSideEffect
import core.usecase.RefreshWrapper
import core.usecase.ResultWithEffect
import features.auth.domain.AuthRepository
import features.auth.domain.model.AuthResult
import features.auth.domain.model.ChangePasswordRequest
import kotlinx.io.IOException


class ChangePasswordUseCase(
    private val repository: AuthRepository,
    private val refreshWrapper: RefreshWrapper
) {
    suspend operator fun invoke(request: ChangePasswordRequest): ResultWithEffect<String, BaseSideEffect> {
        return try {
            val response = repository.changePassword(request)

            response.error?.let { error ->
                when (error.code) {
                    61607 -> refreshWrapper.runWithRefresh { repository.changePassword(request) }
                    else -> ResultWithEffect(sideEffect = BaseSideEffect.ShowError(error.message))
                }
            } ?: ResultWithEffect(result = response.result)
        } catch (e: IOException) {
            ResultWithEffect(sideEffect = BaseSideEffect.ShowError("Нет подключения к интернету"))
        } catch (e: Exception) {
            ResultWithEffect(sideEffect = BaseSideEffect.ShowError("Неизвестная ошибка: ${e.message}"))
        }
    }
}