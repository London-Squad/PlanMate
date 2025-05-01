package di

import data.AuthenticationDataSource
import data.cacheData.CacheDataSource
import data.dataSource.CsvProjectsDataSource
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.repositories.ProjectsRepository
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single {
        val directory = File("csvFiles")
        File(directory, "projects.csv")
    }

    single<ProjectsRepository> { CsvProjectsDataSource(get()) }
    single<CacheDataRepository> { CacheDataSource() }
    single<AuthenticationRepository> { AuthenticationDataSource() }
    single<CacheDataRepository> { CacheDataSource() }
}