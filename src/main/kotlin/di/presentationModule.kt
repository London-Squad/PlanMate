package di

import org.koin.dsl.module
import ui.projectsView.ProjectsView
import logic.usecases.ManageProjectUseCase

val presentationModule = module {
    factory { ProjectsView(manageProjectUseCases = get<ManageProjectUseCase>()) }
}