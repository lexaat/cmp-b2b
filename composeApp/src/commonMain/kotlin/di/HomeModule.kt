package di

import core.error.ApiErrorHandler
import features.home.data.HomeRepositoryImpl
import features.home.domain.repository.HomeRepository
import features.home.presentation.HomeErrorHandler
import features.home.presentation.HomeSideEffect
import features.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    viewModelOf(::HomeViewModel)

    factory<ApiErrorHandler<HomeSideEffect>> { HomeErrorHandler() }
}