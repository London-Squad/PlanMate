package di

import logic.useCases.*
import logic.validation.CredentialValidator
import org.koin.dsl.module

val logicModule = module {
    single { GetLogsByEntityIdUseCase(get()) }
    single { GetLoggedInUserUseCase(get()) }
    single { ProjectUseCases(get(), get(), get()) }
    single { LoginUseCase(get(), get()) }
    single { ManageTaskUseCase(get(), get(), get()) }
    single { ManageStateUseCase(get(), get(), get()) }
    single { CreateMateUseCase(get(), get()) }
    single { CredentialValidator() }
    single { LogoutUseCase(get()) }
}