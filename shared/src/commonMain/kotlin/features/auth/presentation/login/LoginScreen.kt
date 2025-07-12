package features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.error.GlobalErrorHandler
import core.i18n.LocaleController
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import features.auth.presentation.otp.OtpScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.components.AppTopBar
import ui.components.ButtonWithLoader
import ui.components.LanguageSelector
import ui.components.LinkPart
import ui.components.ScreenWrapper
import ui.components.TextWithLinks
import uz.hb.shared.SharedRes

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<LoginViewModel>()

        ScreenWrapper {
            LoginScreenContent(viewModel = viewModel)
        }
    }
}

@Composable
fun LoginScreenContent(viewModel: LoginViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    // Инъекция с параметрами!
    val globalErrorHandler = koinInject<GlobalErrorHandler>(
        parameters = { parametersOf(navigator) }
    )

    viewModel.sideEffect.collectInLaunchedEffect { effect ->
        // 1. Делегируем глобальные сайд-эффекты
        globalErrorHandler.handle(effect)

        // 2. Фичевые эффекты
        when (effect) {
            is LoginSideEffect.NavigateToMain -> navigator.push(MainScreen)
            is LoginSideEffect.NavigateToOtp -> {
                navigator.push(OtpScreen(
                    login = uiState.loginInput,
                    password = uiState.passwordInput,
                    maskedPhoneNumber = effect.phone
                ))
            }

            LoginSideEffect.NavigateBack -> { /* обработано глобально */ }
            LoginSideEffect.SessionExpired -> { /* обработано глобально */ }
            is LoginSideEffect.ShowError -> {
                snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val menuItems = listOf(
        Triple("Настройки", Icons.Default.Settings) { /* navigator.push(SettingsScreen()) */ },
        Triple("О приложении", Icons.Default.Info) { /* showAboutDialog() */ },
        Triple("Выход", Icons.AutoMirrored.Filled.ExitToApp) { /* logout() */ }
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = "",
                onBackClick = null,
                centered = true,
                menuItems = emptyList(),
            )
        },
        contentWindowInsets = WindowInsets.systemBars,
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        LoginForm(
            state = uiState,
            onLoginChanged = { viewModel.processIntent(LoginIntent.LoginInputChanged(it)) },
            onPasswordChanged = { viewModel.processIntent(LoginIntent.PasswordInputChanged(it)) },
            onLoginClicked = { viewModel.processIntent(LoginIntent.LoginButtonClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        )
    }
}

@Composable
fun LoginForm(
    state: LoginState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = koinInject<LoginViewModel>()
    val canUseBiometrics by viewModel.canUseBiometrics.collectAsState()
    val locale by LocaleController.locale.collectAsState()

    key(locale) {
        LoginFormContent(
            login = state.loginInput,
            password = state.passwordInput,
            onLoginChanged = onLoginChanged,
            onPasswordChanged = onPasswordChanged,
            isLoading = state.isLoading,
            canUseBiometrics = canUseBiometrics,
            onLoginClicked = onLoginClicked,
            onBiometricLogin = { viewModel.loginWithBiometrics() },
            modifier = modifier,
            loginLabel = SharedRes.strings.login.desc().localized(),
            passwordLabel = SharedRes.strings.password.desc().localized(),
            loginButtonText = SharedRes.strings.enter.desc().localized(),
            legalEntitiesAppText = SharedRes.strings.legal_entities_app.desc().localized(),
            loginError = state.loginError,
            passwordError = state.passwordError,
            generalError = state.generalError,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = { viewModel.processIntent(LoginIntent.TogglePasswordVisibility) }
        )
    }
}

@Composable
fun LoginFormContent(
    login: String,
    password: String,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    isLoading: Boolean,
    canUseBiometrics: Boolean,
    onLoginClicked: () -> Unit,
    onBiometricLogin: () -> Unit,
    modifier: Modifier = Modifier,
    loginLabel: String,
    passwordLabel: String,
    loginButtonText: String,
    legalEntitiesAppText: String,
    loginError: StringResource?,
    passwordError: StringResource?,
    generalError: StringResource?,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
) {
    val showHelpDialog = remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LanguageSelector(
            modifier = Modifier
                .align(Alignment.End)
        )
        // Элемент, который займет всю оставшуюся высоту
        Box(
            modifier = Modifier
                .fillMaxWidth() // Заполнить ширину
                .weight(2f)
        ) {
            Column(
                modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Заполнить ширину
                        .weight(1f)
                )
            Image(
                painter = painterResource(SharedRes.images.logo_hayot_bank),
                contentDescription = "Hayot Bank",
                modifier = Modifier
                    .size(width = 240.dp, height = 80.dp)
                , // Задаем размер
                contentScale = ContentScale.Fit // Вписываем изображение с сохранением пропорций

            )
                Text(
                    text = legalEntitiesAppText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Заполнить ширину
                        .weight(1f)
                )
            }
        }
        OutlinedTextField(
            value = login,
            onValueChange = onLoginChanged,
            label = { Text(loginLabel) },
            singleLine = true,
//            trailingIcon = {
//                IconButton(onClick = { showHelpDialog.value = true }) {
//                    Icon(
//                        imageVector = Icons.Default.Info,
//                        contentDescription = "Подсказка"
//                    )
//                }
//            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        )

        if (showHelpDialog.value) {
            AlertDialog(
                onDismissRequest = { showHelpDialog.value = false },
                confirmButton = {
                    TextButton(onClick = { showHelpDialog.value = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Подсказка") },
                text = { Text("Введите логин, выданный вам банком. Обычно это email или код пользователя.") }
            )
        }
        if (loginError != null) {
            Text(stringResource(loginError), color = MaterialTheme.colorScheme.error)
        }
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChanged,
            label = { Text(passwordLabel) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (isPasswordVisible) "Скрыть пароль" else "Показать пароль"
                    )
                }
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            )
        )
        if (passwordError != null) {
            Text(stringResource(passwordError), color = MaterialTheme.colorScheme.error)
        }

        if (generalError != null) {
            Text(stringResource(generalError), color = MaterialTheme.colorScheme.error)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth() // Заполнить ширину
                .weight(1f)
        )

        TextWithLinks(
            parts = listOf(
                LinkPart(SharedRes.strings.terms_text_part_1.desc().localized()),
                LinkPart(SharedRes.strings.terms_text_part_2.desc().localized(), tag = "URL", annotation = "https://example.com/1"),
                LinkPart(SharedRes.strings.terms_text_part_3.desc().localized()),
                LinkPart(SharedRes.strings.terms_text_part_4.desc().localized(), tag = "URL", annotation = "https://example.com/2"),
                LinkPart(SharedRes.strings.terms_text_part_5.desc().localized())
            ),
        )

        val isButtonEnabled = login.length >= 4 && password.length >= 4
        ButtonWithLoader(
            onClick = onLoginClicked,
            buttonText = loginButtonText,
            contentColor = MaterialTheme.colorScheme.onSurface,
            showBorder = true,
            showLoader = isLoading,
            enabled = isButtonEnabled,
        )



//        if (canUseBiometrics) {
//            OutlinedButton(
//                onClick = onBiometricLogin,
//
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                Text("Войти по биометрии")
//            }
//        }


    }
}
