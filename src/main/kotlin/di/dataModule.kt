package di

import data.csvDataSource.DtoMapper
import data.repositoriesImpl.*
import data.security.hashing.HashingAlgorithm
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.dsl.module

val dataModule = module {

    single<HashingAlgorithm> { MD5HashingAlgorithm() }
    single { DtoMapper() }

    single<TaskRepository> { TasksRepositoryImpl(get(), get(), get()) }
    single<TasksStatesRepository> { TasksStatesRepositoryImpl(get(), get(), get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(), get(), get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
    single<LogsRepository> { LogsRepositoryImpl(get(), get(), get(), get(), get(), get()) }

}