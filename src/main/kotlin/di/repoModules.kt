package di

import data.cacheData.CacheDataRepositoryImpl
import data.repositoriesImpl.CsvProjectsRepository
import logic.repositories.CacheDataRepository
import logic.repositories.ProjectsRepository
import org.koin.dsl.module

val repoModules = module {
    single<ProjectsRepository> { CsvProjectsRepository() }
    single<CacheDataRepository> { CacheDataRepositoryImpl() }

}