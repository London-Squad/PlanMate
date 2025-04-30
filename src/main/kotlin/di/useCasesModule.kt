package di

import logic.repositories.ProjectsRepository
import logic.usecases.ManageProjectUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single { ManageProjectUseCase(projectRepository = get()) }
}
