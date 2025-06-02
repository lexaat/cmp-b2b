package di

import core.error.ApiCallHandler
import features.auth.domain.usecase.LoginUseCase
import features.auth.domain.usecase.RefreshTokenUseCase
import features.auth.domain.usecase.ChangePasswordUseCase
import features.auth.presentation.login.AuthViewModel
import features.auth.presentation.otp.OtpViewModel
import features.auth.presentation.password.change.PasswordChangeViewModel
import org.koin.dsl.module

val authModule = module {

    factory { LoginUseCase(get()) }
    factory { RefreshTokenUseCase(get()) }
    factory { ChangePasswordUseCase(get()) }

    single { ApiCallHandler(get(), get(), get()) }

    single { AuthViewModel(get(), get(), get(), get(), get()) }
    single { OtpViewModel(get(), get(), get()) }
    single { PasswordChangeViewModel(get(), get()) }
}