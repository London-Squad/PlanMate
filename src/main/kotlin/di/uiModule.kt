package di

import org.koin.dsl.module
import ui.projectsView.ProjectsView

val uiModule = module {
    single { ProjectsView(get(), get(), get()) }

}