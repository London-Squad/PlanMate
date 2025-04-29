package di

import logic.useCases.ProjectUseCases
import main.logic.useCases.LogUseCases
import main.logic.useCases.StateUseCases
import main.logic.useCases.TaskUseCases
import org.koin.dsl.module

val useCaseModule = module {
    single { ProjectUseCases(get()) }
    single { TaskUseCases() }
    single { StateUseCases() }
    single { LogUseCases() }
}