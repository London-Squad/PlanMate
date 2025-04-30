package di

import org.koin.dsl.module
import data.csv.CsvProjectHandler
import data.repositories.ProjectRepositoryImpl
import logic.repositories.ProjectsRepository
import ui.projectsView.ProjectsView
import ui.projectsView.ProjectsViewHandler

val appModule = module {
    single { CsvProjectHandler(filePath = "projects.csv") }
    single { ProjectsViewHandler(get()) }
    single { ProjectsView(get()) }
}