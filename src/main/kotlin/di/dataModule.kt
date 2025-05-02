package di

import data.TaskDataSource
import data.cacheData.CacheDataSource
import data.dataSource.CsvProjectsDataSource
import data.dataSource.CsvStatesDataSource
import data.fileIO.FilePath
import data.logsRepositories.csvFilesHandler.LogsDataSource
import data.logsRepositories.cvsFilesHandler.LogsCsvReader
import data.logsRepositories.cvsFilesHandler.LogsCsvWriter
import data.repository.AuthenticationDataSource
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single {
        val directory = File("csvFiles")
        File(directory, "projects.csv")
    }

    single(named("LogsFile")) {
        val directory = File("csvFiles")
        File(directory, "logs.csv")
    }

    single<StatesRepository> { CsvStatesDataSource() }
    single<ProjectsRepository> { CsvProjectsDataSource(get()) }

    single { LogsCsvReader(get(named("LogsFile"))) }
    single { LogsCsvWriter(get(named("LogsFile"))) }

    single<LogsRepository> { LogsDataSource(get(), get(), get(), get(), get()) }

    single<CacheDataRepository> { CacheDataSource(File(FilePath.ACTIVE_USER_FILE)) }

    single<AuthenticationRepository> { AuthenticationDataSource(File(FilePath.USER_FILE), MD5HashingAlgorithm()) }

    single<TaskRepository> { TaskDataSource() }
}