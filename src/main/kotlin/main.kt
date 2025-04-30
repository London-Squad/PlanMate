package main

import di.appModule
import di.repoModules
import di.uiModule
import di.useCaseModule
import logic.entities.User
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.projectsView.ProjectsView

fun main() {
    startKoin {
        modules(appModule, repoModules, uiModule, useCaseModule)
    }
}