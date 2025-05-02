package di

import logic.useCases.*
import org.koin.dsl.module

val logicModule = module {
    single { GetLogsByEntityIdUseCase(get()) }
    single { ClearLoggedInUserFromCacheUseCase(get()) }
    single { GetLoggedInUserUseCase(get()) }
    single { ProjectUseCases(get(), get(), get()) }
    single { LoginUseCase(get(), get()) }
    single { ManageTaskUseCase(get()) }
    single { ManageStateUseCase(get(), get(), get()) }
}