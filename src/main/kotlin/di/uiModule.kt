package di

import logic.entities.Project
import logic.entities.User
import org.koin.dsl.module
import ui.projectView.ProjectView
import ui.projectsView.ProjectsView

val uiModule = module {
    factory { (currentUser: User) ->
        ProjectsView(get(), get(), get(), get(), get(), get(), currentUser)
    }

    factory { (project: Project, currentUser: User) ->
        ProjectView(project, get(), get(), get(), get(), get(), get(), currentUser)
    }
}