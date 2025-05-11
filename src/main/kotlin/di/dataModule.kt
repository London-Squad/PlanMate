package di

import data.repositories.*
import data.security.hashing.HashingAlgorithm
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.dsl.module

val dataModule = module {

    single<HashingAlgorithm> { MD5HashingAlgorithm() }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get()) }
    single<UserRepository>{ UserRepositoryImpl(get(),get())}

}