package di

import features.home.data.HomeRepositoryImpl
import features.home.domain.repository.HomeRepository
import features.home.domain.usecase.GetClientsUseCase
import features.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl(
            httpClient = get(),
            config = get()
        )
    }
    single { GetClientsUseCase(get()) }
    viewModel { HomeViewModel(get()) }

}
