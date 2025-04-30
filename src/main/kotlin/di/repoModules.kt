package di

import data.cacheData.CacheDataRepositoryImpl
import data.repositoriesImpl.CsvProjectsRepository
import logic.repositories.CacheDataRepository
import logic.repositories.ProjectsRepository
import org.koin.dsl.module
import java.io.File

val repoModules = module {
    single {
        val directory = File("csvFiles")
        File(directory, "projects.csv")
    }

    single<ProjectsRepository> { CsvProjectsRepository(get()) }
    single<CacheDataRepository> { CacheDataRepositoryImpl() }
}