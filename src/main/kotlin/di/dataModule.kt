package di

import data.CacheDataSource
import data.CsvProjectsDataSource
import data.CsvStatesDataSource
import data.CsvTasksDataSource
import logic.repositories.*
import data.fileIO.FilePath
import data.LogsDataSource
import data.fileIO.cvsLogsFileHandler.LogsCsvReader
import data.fileIO.cvsLogsFileHandler.LogsCsvWriter
import data.AuthenticationDataSource
import data.security.hashing.MD5HashingAlgorithm
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

    single(named("LogsFile")) {
        val directory = File("csvFiles")
        File(directory, "logs.csv")
    }

    single<TaskRepository> { CsvTasksDataSource(get(named("tasksFile")), get()) }
    single<StatesRepository> { CsvStatesDataSource(get(named("statesFile"))) }
    single<ProjectsRepository> {
        CsvProjectsDataSource(
            get(named("projectsFile")),
            get<TaskRepository>() as CsvTasksDataSource,
            get<StatesRepository>() as CsvStatesDataSource
        )
    }

    single { LogsCsvReader(get(named("LogsFile"))) }
    single { LogsCsvWriter(get(named("LogsFile"))) }

    single<LogsRepository> { LogsDataSource(get(), get(), get(), get()) }

    single<CacheDataRepository> { CacheDataSource(File(FilePath.ACTIVE_USER_FILE)) }

    single<AuthenticationRepository> { AuthenticationDataSource(File(FilePath.USER_FILE), MD5HashingAlgorithm()) }

}