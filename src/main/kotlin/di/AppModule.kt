package di

import org.koin.dsl.module
import data.csv.CsvProjectHandler
import data.repositories.ProjectRepositoryImpl
import logic.repositories.ProjectsRepository

val appModule = module {
    single { CsvProjectHandler(filePath = "projects.csv") }
    single<ProjectsRepository> { ProjectRepositoryImpl(get()) }
}
