package di

import logic.useCases.ManageTaskUseCase
import logic.useCases.ProjectUseCases
import logic.useCases.loginUseCase.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { ProjectUseCases(get()) }
    single { LoginUseCase(get()) }
    single { ManageTaskUseCase(get()) }
}