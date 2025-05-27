package core.error

import core.model.ResultWithEffect
import core.presentation.BaseSideEffect
import data.model.ApiResponse

interface ApiErrorHandler<SIDE_EFFECT : BaseSideEffect> {
    suspend fun <T> handleApiCall(
        call: suspend () -> ApiResponse<T>,
        retry: suspend () -> ApiResponse<T>
    ): ResultWithEffect<T, SIDE_EFFECT>
}
