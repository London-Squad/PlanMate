package di

import data.csvDataSource.*
import data.csvDataSource.fileIO.*
import data.csvDataSource.fileIO.cvsLogsFileHandler.LogsCsvReader
import data.csvDataSource.fileIO.cvsLogsFileHandler.LogsCsvWriter
import data.dataSources.*
import data.repositoriesImpl.*
import data.security.hashing.HashingAlgorithm
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {

    single<HashingAlgorithm> { MD5HashingAlgorithm() }

    single<TaskRepository> { TasksRepositoryImpl(get(), get(), get()) }
    single<TasksStatesRepository> { TasksStatesRepositoryImpl(get(), get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(), get(), get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
    single<LogsRepository> { LogsRepositoryImpl(get(), get(), get(), get(), get(), get()) }

}