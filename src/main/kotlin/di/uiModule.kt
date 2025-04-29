package di

import logic.repositories.CacheDataRepository
import data.cacheData.CacheDataRepositoryImpl
import org.koin.dsl.module
import ui.projectView.ProjectView
import ui.projectsView.ProjectsView

val uiModule = module {
    single { ProjectsView(get(), get(), get(), get(), get(), get(), get()) }
    single { ProjectView(get(), get(), get(), get(), get(), get(), get()) }
}