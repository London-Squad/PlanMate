
package di

import data.repositories.ProjectRepositoryImpl
import data.csv.CsvProjectHandler
import logic.repositories.ProjectsRepository
import org.koin.dsl.module

val dataModule = module {
    single { CsvProjectHandler(filePath = "projects.csv") }
    single<ProjectsRepository> { ProjectRepositoryImpl(projectHandler = get()) }
}