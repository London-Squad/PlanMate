package di

import logic.useCases.*
import org.koin.dsl.module

val logicModule = module {
    single { ClearLoggedInUserFromCacheUseCase(get()) }
    single { GetLoggedInUserUseCase(get()) }
    single { ProjectUseCases(get()) }
    single { LoginUseCase(get(), get()) }
    single { ManageTaskUseCase(get()) }
    single { ManageStateUseCase(get(), get(), get()) }
}