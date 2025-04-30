package di

import org.koin.dsl.module
import ui.projectView.ProjectView
import ui.projectsView.ProjectsView

val uiModule = module {
    single { ProjectsView(get(), get(), get(), get(), get()) }
    single { ProjectView(get(), get(), get(), get(), get(), get(), get()) }
}