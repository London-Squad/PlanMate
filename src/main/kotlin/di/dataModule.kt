package di

import data.AuthenticationDataSource
import data.cacheData.CacheDataSource
import data.dataSource.CsvProjectsDataSource
import data.dataSource.CsvStatesDataSource
import data.dataSource.CsvTasksDataSource
import logic.repositories.*
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<StatesRepository> { CsvStatesDataSource() }
    single<TaskRepository> { CsvTasksDataSource(File("csvFiles", "tasks.csv"), get()) }
    single<ProjectsRepository> { CsvProjectsDataSource(File("csvFiles", "projects.csv")) }
    single<CacheDataRepository> { CacheDataSource() }
    single<AuthenticationRepository> { AuthenticationDataSource() }
}