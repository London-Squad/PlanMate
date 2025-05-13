package di

import GetUserDataUseCase
import logic.useCases.*
import logic.validation.CredentialValidator
import logic.validation.ProjectInputValidator
import logic.validation.TaskInputValidator
import logic.validation.TaskStateInputValidator
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
    single { CreateMateUseCase(get(), get(), get()) }
    single { CredentialValidator() }
    single { ProjectInputValidator() }
    single { TaskInputValidator() }
    single { TaskStateInputValidator() }
    single { LogoutUseCase(get()) }
    single { GetAllMatesUseCase(get()) }
    single { GetProjectDetailsUseCase(get(), get(), get()) }
    single { GetUserDataUseCase(get()) }
}