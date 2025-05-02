package di

import data.TaskDataSource
import data.cacheData.CacheDataSource
import data.dataSource.CsvProjectsDataSource
import data.dataSource.CsvStatesDataSource
import data.dataSource.LogsDataSource
import data.fileIO.FilePath
import data.repository.AuthenticationDataSource
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single {
        val directory = File("csvFiles")
        File(directory, "projects.csv")
    }

    single<StatesRepository> { CsvStatesDataSource() }
    single<LogsRepository> { LogsDataSource() }
    single<ProjectsRepository> { CsvProjectsDataSource(get()) }
    single<CacheDataRepository> { CacheDataSource(File(FilePath.ACTIVE_USER_FILE)) }
    single<AuthenticationRepository> { AuthenticationDataSource(File(FilePath.USER_FILE), MD5HashingAlgorithm()) }
    single<CacheDataRepository> { CacheDataSource(File(FilePath.ACTIVE_USER_FILE)) }
    single<TaskRepository> { TaskDataSource() }
}