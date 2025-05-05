package di

import data.csvDataSource.*
import data.csvDataSource.fileIO.*
import data.csvDataSource.fileIO.cvsLogsFileHandler.LogsCsvReader
import data.csvDataSource.fileIO.cvsLogsFileHandler.LogsCsvWriter
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
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
    single<TasksStatesRepository> { CsvTasksStatesDataSource(get(named("statesFile"))) }
    single<ProjectsRepository> {
        CsvProjectsDataSource(
            get(named("projectsFile")),
            get<TaskRepository>() as CsvTasksDataSource,
            get<TasksStatesRepository>() as CsvTasksStatesDataSource
        )
    }

    single { LogsCsvReader(get(named("LogsFile"))) }
    single { LogsCsvWriter(get(named("LogsFile"))) }

    single<LogsRepository> { CsvLogsDataSource(get(), get(), get(), get(), get(), get()) }


    single<AuthenticationRepository> {
        CsvAuthenticationDataSource(
            File(FilePath.USER_FILE),
            File(FilePath.ACTIVE_USER_FILE),
            MD5HashingAlgorithm(),
        )
    }

}