package di

import logic.useCases.ProjectUseCases
import org.koin.dsl.module

val useCaseModule = module {
    single { ProjectUseCases(get()) }
}