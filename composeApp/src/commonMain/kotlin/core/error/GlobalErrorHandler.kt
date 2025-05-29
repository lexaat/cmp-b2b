package core.error

import cafe.adriel.voyager.navigator.Navigator
import core.presentation.BaseSideEffect
import features.auth.presentation.AuthScreen
import features.common.domain.auth.TokenManager

class GlobalErrorHandler(
    private val navigator: Navigator,
    private val tokenManager: TokenManager
) {
    suspend fun handle(effect: BaseSideEffect) {
        when (effect) {
            BaseSideEffect.SessionExpired -> {
                tokenManager.clearAccessToken()
                navigator.replaceAll(AuthScreen)
            }
            is BaseSideEffect.ShowError -> {
            }
            BaseSideEffect.NavigateBack -> {
                navigator.pop()
            }
            else -> { }
        }
    }
}