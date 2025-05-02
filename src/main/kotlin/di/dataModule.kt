package di

import data.TaskDataSource
import data.cacheData.CacheDataSource
import data.dataSource.CsvProjectsDataSource
import data.dataSource.CsvStatesDataSource
import data.dataSource.CsvStatesDataSource
import data.dataSource.LogsDataSource
import data.fileIO.FilePath
import data.repository.AuthenticationDataSource
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.repositories.TaskRepository
import org.koin.core.qualifier.named
import logic.repositories.*
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single(named("projectsFile")) {
        val directory = File("csvFiles")
        File(directory, "projects.csv")
    }

    single(named("statesFile")) {
        val directory = File("csvFiles")
        File(directory, "states.csv")
    }

    single<ProjectsRepository> { CsvProjectsDataSource(get(named("projectsFile"))) }
    single<StatesRepository> { CsvStatesDataSource(get(named("statesFile"))) }
    single<StatesRepository> { CsvStatesDataSource() }
    single<LogsRepository> { LogsDataSource() }
    single<ProjectsRepository> { CsvProjectsDataSource(get()) }
    single<CacheDataRepository> { CacheDataSource(File(FilePath.ACTIVE_USER_FILE)) }
    single<AuthenticationRepository> { AuthenticationDataSource(File(FilePath.USER_FILE), MD5HashingAlgorithm()) }
    single<TaskRepository> { TaskDataSource() }
}