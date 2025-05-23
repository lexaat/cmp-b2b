package core.presentation

import androidx.lifecycle.ViewModel
import core.error.ApiError
import core.error.ApiErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<SIDE_EFFECT : BaseSideEffect>(
    private val errorHandler: ApiErrorHandler<SIDE_EFFECT>,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    protected val _sideEffect = MutableSharedFlow<SIDE_EFFECT>()
    val sideEffect: SharedFlow<SIDE_EFFECT> = _sideEffect

    protected fun handleError(error: ApiError) {
        coroutineScope.launch {
            errorHandler.handleApiError(error) { _sideEffect.emit(it) }
        }
    }
}