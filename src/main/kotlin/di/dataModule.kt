package di

import data.cacheData.CacheDataSource
import data.dataSource.CsvProjectsDataSource
import data.dataSource.CsvStatesDataSource
import data.dataSource.CsvTasksDataSource
import logic.repositories.*
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
import org.koin.dsl.module
import java.io.File

val dataModule = module {

    single(named("statesFile")) {
        val directory = File("csvFiles")
        File(directory, "states.csv")
    }

    single(named("tasksFile")) {
        val directory = File("csvFiles")
        File(directory, "tasks.csv")
    }

    single(named("projectsFile")) {
        val directory = File("csvFiles")
        File(directory, "projects.csv")
    }

    single<TaskRepository> { CsvTasksDataSource(get(named("tasksFile")), get()) }
    single<StatesRepository> { CsvStatesDataSource(get(named("statesFile"))) }
    single<ProjectsRepository> { CsvProjectsDataSource(get(named("projectsFile"))) }
    single<LogsRepository> { LogsDataSource() }
    single<CacheDataRepository> { CacheDataSource(File(FilePath.ACTIVE_USER_FILE)) }
    single<AuthenticationRepository> { AuthenticationDataSource(File(FilePath.USER_FILE), MD5HashingAlgorithm()) }
}