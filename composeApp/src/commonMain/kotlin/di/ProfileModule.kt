package di

import features.profile.presentation.ProfileViewModel
import org.koin.dsl.module


val profileModule = module {
    single { ProfileViewModel(get()) }
}