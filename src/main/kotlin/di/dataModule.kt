package di

import data.repositories.*
import data.security.hashing.HashingAlgorithm
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.dsl.module

val dataModule = module {

    single<HashingAlgorithm> { MD5HashingAlgorithm() }

    single<TaskRepository> { TasksRepositoryImpl(get(), get()) }
    single<TaskStatesRepository> { TaskStatesRepositoryImpl(get(), get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(), get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get()) }
    single<LogsRepository> { LogsRepositoryImpl(get(), get(), get(), get(), get()) }

}