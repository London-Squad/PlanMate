package di

import com.mongodb.client.MongoDatabase
import data.dataSources.*
import data.mongoDBDataSource.*
import data.repositoriesImpl.*
import data.security.hashing.HashingAlgorithm
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.dsl.module

val dataModule = module {

    // Security
    single<HashingAlgorithm> { MD5HashingAlgorithm() }


    // Repository Implementations
    single<TaskRepository> { TasksRepositoryImpl(get(), get(), get()) }
    single<TasksStatesRepository> { TasksStatesRepositoryImpl(get(), get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(), get(), get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
    single<LogsRepository> { LogsRepositoryImpl(get(), get(), get(), get(), get(), get()) }


}