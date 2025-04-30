package di

import data.AuthenticationDataSource
import data.cacheData.CacheDataRepositoryImpl
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationRepository> { AuthenticationDataSource() }
    single<CacheDataRepository> { CacheDataRepositoryImpl() }
}