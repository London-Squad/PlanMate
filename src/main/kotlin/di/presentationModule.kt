package di

import org.koin.dsl.module
import ui.projectsView.ProjectsView
import ui.projectsView.ProjectsViewHandler

val presentationModule = module {
    factory { ProjectsView(handler = get()) }
    factory { ProjectsViewHandler(
        manageProjectUseCases = get(),
    ) }

}