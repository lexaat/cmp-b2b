package di

import features.auth.domain.usecase.LoginUseCase
import features.auth.domain.usecase.RefreshTokenUseCase
import features.auth.domain.usecase.ChangePasswordUseCase
import features.auth.presentation.login.LoginViewModel
import features.auth.presentation.otp.OtpViewModel
import features.auth.presentation.password.change.PasswordChangeRequestViewModel
import features.auth.presentation.password.confirm.PasswordChangeOtpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    factory { LoginUseCase(get(), get()) }
    factory { RefreshTokenUseCase(get()) }
    factory { ChangePasswordUseCase(get()) }

    single { LoginViewModel(
        loginUseCase = get(),
        refreshTokenUseCase = get(),
        tokenManager = get(),
        biometricAuthenticator = get(),
        secureStorage = get ()) }
    viewModel { (login: String, password: String) ->
        OtpViewModel(
            loginUseCase = get(),
            tokenManager = get(),
            secureStorage = get (),
            login = login,
            password = password
        )
    }
    viewModel { (login: String, password: String) ->
        PasswordChangeRequestViewModel(
            login = login,
            password = password,
            changePasswordUseCase = get()
        )
    }
    viewModel { (login: String, newPassword: String, oldPassword: String) ->
        PasswordChangeOtpViewModel(
            changePasswordUseCase = get(),
            login = login,
            newPassword = newPassword,
            oldPassword = oldPassword
        )
    }
}