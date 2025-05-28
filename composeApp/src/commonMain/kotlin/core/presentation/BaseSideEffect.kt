package core.presentation

interface BaseSideEffect {
    object SessionExpired : BaseSideEffect
    object NavigateBack   : BaseSideEffect
    data class ShowError(val message: String) : BaseSideEffect
    //object NavigateToMain : BaseSideEffect
    //object NavigateToOtp : BaseSideEffect
    //object NavigateToPasswordChange : BaseSideEffect
}
