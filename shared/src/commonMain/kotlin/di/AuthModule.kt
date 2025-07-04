package di

import features.auth.domain.usecase.LoginUseCase
import features.auth.domain.usecase.RefreshTokenUseCase
import features.auth.domain.usecase.ChangePasswordUseCase
import features.auth.presentation.login.LoginViewModel
import features.auth.presentation.otp.OtpViewModel
import features.auth.presentation.password.change.ChangePasswordViewModel
import features.auth.presentation.password.otp.PasswordOtpViewModel
import org.koin.dsl.module

val authModule = module {

    factory { LoginUseCase(get(), get()) }
    factory { RefreshTokenUseCase(get()) }
    factory { ChangePasswordUseCase(get(), get()) }

    single { LoginViewModel(get(), get(), get(), get()) }
    single { OtpViewModel(get(), get()) }
    single { ChangePasswordViewModel(get()) }
    single { PasswordOtpViewModel(get()) }
}