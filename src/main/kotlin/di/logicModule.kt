package di

import logic.useCases.loginUseCase.LoginUseCase
import org.koin.dsl.module

val logicModule = module {
    single { LoginUseCase(get()) }
}