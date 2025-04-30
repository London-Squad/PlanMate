package di

import org.koin.dsl.module
import logic.usecases.ManageProjectUseCase

val useCasesModule = module {
    factory { ManageProjectUseCase(projectRepository = get()) }
}