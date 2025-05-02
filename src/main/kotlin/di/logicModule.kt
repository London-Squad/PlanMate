package di

import logic.useCases.ClearLoggedInUserFromCacheUseCase
import logic.useCases.GetLoggedInUserUseCase
import logic.useCases.ManageTaskUseCase
import logic.useCases.ProjectUseCases
import logic.useCases.LoginUseCase
import org.koin.dsl.module

val logicModule = module {
    single { ClearLoggedInUserFromCacheUseCase(get()) }
    single { GetLoggedInUserUseCase(get()) }
    single { ProjectUseCases(get()) }
    single { LoginUseCase(get(), get()) }
    single { ManageTaskUseCase(get()) }
}