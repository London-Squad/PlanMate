package di

import data.repositoriesImpl.CsvProjectsRepository
import logic.repositories.ProjectsRepository
import org.koin.dsl.module

val repoModules = module {
    single<ProjectsRepository> { CsvProjectsRepository() }

}