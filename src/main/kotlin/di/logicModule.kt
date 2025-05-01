package di

import logic.useCases.ManageTaskUseCase
import logic.useCases.LoginUseCase
import logic.useCases.SignupUseCase
import org.koin.dsl.module

val logicModule = module {
    single { LoginUseCase(get(),get()) }
    single { SignupUseCase(get()) }
    single { ManageTaskUseCase(get()) }
}