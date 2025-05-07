package di

import logic.useCases.*
import logic.useCases.mateUseCase.CreateMateUseCase
import logic.validation.CredentialValidator
import logic.validation.UserInputValidator
import org.koin.dsl.module

val logicModule = module {
    single { GetLogsByEntityIdUseCase(get()) }
    single { CreateLogUseCase(get(), get()) }
    single { GetLoggedInUserUseCase(get()) }
    single { ManageProjectUseCase(get(), get()) }
    single { CreateProjectUseCase(get(), get(), get()) }
    single { LoginUseCase(get()) }
    single { ManageTaskUseCase(get(), get(), get()) }
    single { ManageStateUseCase(get(), get()) }
    single { CreateMateUseCase(get(), get()) }
    single { CredentialValidator() }
    single { UserInputValidator() }
    single { LogoutUseCase(get()) }
    single { GetAllMatesUseCase(get()) }
}