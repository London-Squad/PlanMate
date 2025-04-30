package di

import data.AuthenticationDataSource
import data.cacheData.CacheDataSource
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationRepository> { AuthenticationDataSource() }
    single<CacheDataRepository> { CacheDataSource() }
}