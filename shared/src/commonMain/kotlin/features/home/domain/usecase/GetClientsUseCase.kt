package features.home.domain.usecase

import core.presentation.BaseSideEffect
import core.usecase.RefreshWrapper
import core.usecase.ResultWithEffect
import domain.model.Client
import features.home.domain.repository.HomeRepository
import kotlinx.io.IOException

class GetClientsUseCase(private val repository: HomeRepository,
                        private val refreshWrapper: RefreshWrapper
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): ResultWithEffect<List<Client>, BaseSideEffect> {
        return try {
            val response = repository.getClients(forceRefresh)

            response.error?.let { error ->
                when (error.code) {
                    61607 -> refreshWrapper.runWithRefresh { repository.getClients(forceRefresh = forceRefresh) }
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