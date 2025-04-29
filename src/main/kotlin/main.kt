package main

import di.appModule
import di.repoModules
import di.uiModule
import di.useCaseModule
import logic.entities.User
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import ui.projectsView.ProjectsView

fun main() {
    startKoin {
        modules(appModule, repoModules, uiModule, useCaseModule)
    }
    val usser = User(
        userName = "admin",
        type = User.Type.ADMIN
    )

    val projectsView: ProjectsView = getKoin().get { parametersOf(usser) }

    projectsView.start()
}