package core.error

import core.presentation.BaseSideEffect

interface ApiErrorHandler<SIDE_EFFECT : BaseSideEffect> {
    suspend fun handleApiError(error: ApiError, emitEffect: suspend (SIDE_EFFECT) -> Unit)
}