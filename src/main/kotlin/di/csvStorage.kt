package di

import data.dataSources.csvDataSource.*
import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dto.UserCsvDto
import data.repositories.AuthenticationRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.dataSources.*
import data.repositories.dtoMappers.user.UserCsvDtoMapper
import data.repositories.dtoMappers.user.UserDtoMapper
import logic.repositories.*
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



    single<TaskRepository> { CsvTasksDataSource(get(named("tasksFileHandler")), get()) }
    single<TaskStatesRepository> { CsvTaskStatesDataSource(get(named("taskStatesFileHandler")), get()) }
    single<ProjectsRepository> { CsvProjectsDataSource(get(named("projectsFileHandler")), get()) }
    single<UsersDataSource<UserCsvDto>> { CsvUsersDataSource(get(named("usersFileHandler")), get()) }
    single<LogsRepository> { CsvLogsDataSource(get(named("LogsFileHandler")), get(), get(), get(), get()) }

    single<UserDtoMapper<UserCsvDto>> { UserCsvDtoMapper() }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl<UserCsvDto>(get(), get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl<UserCsvDto>(get(), get(), get()) }

}