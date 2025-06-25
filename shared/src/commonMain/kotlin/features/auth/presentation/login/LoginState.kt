package features.auth.presentation.login

import dev.icerock.moko.resources.StringResource

data class LoginState(
    val loginInput: String = "",
    val passwordInput: String = "",
    val loginError: StringResource? = null, // Можно использовать Resource ID для локализации
    val passwordError: StringResource? = null, // Можно использовать Resource ID
    val isLoading: Boolean = false,
    val generalError: StringResource? = null, // Общая ошибка (например, от сервера)
    // Дополнительные поля:
    val isPasswordVisible: Boolean = false,
    val canUseBiometrics: Boolean = false, // Это у вас в ViewModel, может остаться там или дублироваться здесь для UI
    // val navigateToOtp: Boolean = false, // Лучше через SideEffect
    // val navigateToMain: Boolean = false // Лучше через SideEffect
)