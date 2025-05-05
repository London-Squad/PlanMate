package di

import data.*
import data.fileIO.FilePath
import data.fileIO.cvsLogsFileHandler.LogsCsvReader
import data.fileIO.cvsLogsFileHandler.LogsCsvWriter
import data.AuthenticationDataSource
import data.csvHandler.CsvFileHandler
import data.parser.ProjectParser
import data.parser.StateParser
import data.parser.TaskParser
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

    single(named("logsFile")) {
        val directory = File("csvFiles")
        File(directory, "logs.csv")
    }

    single { CsvFileHandler(get(named("statesFile"))) }
    single(named("tasksFileHandler")) { CsvFileHandler(get(named("tasksFile"))) }
    single(named("projectsFileHandler")) { CsvFileHandler(get(named("projectsFile"))) }
    single(named("statesFileHandler")) { CsvFileHandler(get(named("statesFile"))) }
    single(named("logsFileHandler")) { CsvFileHandler(get(named("logsFile"))) }

    single { TaskParser(get(), get()) }
    single { StateParser(get()) }
    single {
        ProjectParser(
            get<TaskRepository>() as CsvTasksDataSource,
            get<StatesRepository>() as CsvStatesDataSource,
            get()
        )
    }

    single<TaskRepository> {
        CsvTasksDataSource(
            fileHandler = get(named("tasksFileHandler")),
            taskParser = get(),
        )
    }

    single<StatesRepository> {
        CsvStatesDataSource(
            fileHandler = get(named("statesFileHandler")),
            stateParser = get()
        )
    }

    single<ProjectsRepository> {
        CsvProjectsDataSource(
            fileHandler = get(named("projectsFileHandler")),
            projectParser = get(),
            taskRepository = get<TaskRepository>() as CsvTasksDataSource,
            statesRepository = get<StatesRepository>() as CsvStatesDataSource
        )
    }

    single { LogsCsvReader(get(named("logsFile"))) }
    single { LogsCsvWriter(get(named("logsFile"))) }

    single<LogsRepository> { LogsDataSource(get(), get(), get(), get(), get(), get()) }


    single<AuthenticationRepository> {
        AuthenticationDataSource(
            File(FilePath.USER_FILE),
            File(FilePath.ACTIVE_USER_FILE),
            MD5HashingAlgorithm(),
        )
    }

}