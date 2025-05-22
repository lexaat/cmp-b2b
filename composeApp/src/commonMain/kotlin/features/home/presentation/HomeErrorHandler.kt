package features.home.presentation

import core.error.ApiError
import core.error.ApiErrorHandler

class HomeErrorHandler : ApiErrorHandler<HomeSideEffect> {
    override suspend fun handleApiError(error: ApiError, emitEffect: suspend (HomeSideEffect) -> Unit) {
        emitEffect(HomeSideEffect.ShowHomeError("Ошибка: ${error.message}"))
    }
}
