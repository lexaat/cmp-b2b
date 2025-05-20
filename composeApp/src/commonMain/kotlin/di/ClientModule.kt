package di

import features.client.data.ClientRepositoryImpl
import features.client.domain.repository.ClientRepository
import features.client.presentation.ClientDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val clientModule = module {
    single<ClientRepository> { ClientRepositoryImpl(get()) }
    viewModelOf(::ClientDetailViewModel)
}
