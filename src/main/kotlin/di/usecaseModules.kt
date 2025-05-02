package di

import logic.useCases.ClearLoggedInUserFromCacheUseCase
import logic.useCases.GetLoggedInUserUseCase
import logic.useCases.ManageTaskUseCase
import logic.useCases.ProjectUseCases
import logic.useCases.loginUseCase.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { ClearLoggedInUserFromCacheUseCase(get()) }
    single { GetLoggedInUserUseCase(get()) }
    single { ProjectUseCases(get()) }
    single { LoginUseCase(get()) }
    single { ManageTaskUseCase(get()) }
}