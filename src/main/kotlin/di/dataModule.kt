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
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<StatesRepository> { CsvStatesDataSource() }
    single<TaskRepository> { CsvTasksDataSource(File("csvFiles", "tasks.csv"), get()) }
    single<ProjectsRepository> { CsvProjectsDataSource(File("csvFiles", "projects.csv")) }

    single<LogsRepository> { LogsDataSource() }
    single<CacheDataRepository> { CacheDataSource(File(FilePath.ACTIVE_USER_FILE)) }
    single<AuthenticationRepository> { AuthenticationDataSource(File(FilePath.USER_FILE), MD5HashingAlgorithm()) }
}