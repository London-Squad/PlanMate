package di

import logic.useCases.*
import logic.useCases.CreateMateUseCase
import logic.validation.CredentialValidator
import logic.validation.UserInputValidator
import org.koin.dsl.module

val logicModule = module {
    single { GetLogsByEntityIdUseCase(get()) }
    single { ClearLoggedInUserFromCacheUseCase(get()) }
    single { GetLoggedInUserUseCase(get()) }
    single { ProjectUseCases(get(), get(), get()) }
    single { LoginUseCase(get(), get(), get()) }
    single { ManageTaskUseCase(get(), get(), get()) }
    single { ManageStateUseCase(get(), get(), get()) }
    single { CreateMateUseCase(get(), get(), get()) }
    single { CredentialValidator() }
    single { UserInputValidator() }
}