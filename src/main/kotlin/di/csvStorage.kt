package di

import data.dataSources.csvDataSource.*
import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.dataSourceInterfaces.*
import logic.repositories.TaskRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val csvStorageModule = module {

    single(named("tasksFileHandler")) {
        val directory = File("csvFiles")
        CsvFileHandler(File(directory, "tasks.csv"))
    }

    single(named("taskStatesFileHandler")) {
        val directory = File("csvFiles")
        CsvFileHandler(File(directory, "taskStatesFile.csv"))
    }

    single(named("projectsFileHandler")) {
        val directory = File("csvFiles")
        CsvFileHandler(File(directory, "projects.csv"))
    }

    single(named("LogsFileHandler")) {
        val directory = File("csvFiles")
        CsvFileHandler(File(directory, "logs.csv"))
    }

    single(named("usersFileHandler")) {
        val directory = File("csvFiles")
        CsvFileHandler(File(directory, "users.csv"))
    }

    single(named("activeUserFileHandler")) {
        val directory = File("csvFiles")
        CsvFileHandler(File(directory, "activeUser.csv"))
    }

    single { CsvParser() }

    single<TaskRepository> { CsvTasksDataSource(get(named("tasksFileHandler")), get()) }

    single<TaskStatesDataSource> { CsvTaskStatesDataSource(get(named("taskStatesFileHandler")), get()) }
    single<ProjectsDataSource> { CsvProjectsDataSource(get(named("projectsFileHandler")), get()) }
    single<ProjectsDataSource> { CsvProjectsDataSource(get(), get()) }
    single<UsersDataSource> {
        CsvUsersDataSource(
            get(named("usersFileHandler")),
            get(named("activeUserFileHandler")),
            get()
        )
    }
    single<LogsDataSource> { CsvLogsDataSource(get(named("LogsFileHandler")), get()) }

}