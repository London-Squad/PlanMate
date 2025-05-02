package di

import data.security.hashing.MD5HashingAlgorithm
import logic.usecases.CreateMateUseCase
import org.koin.dsl.module
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.matesManagementView.MateCreationView
import ui.matesManagementView.MatesManagementView

val appModule = module {
    single { CLIPrinter() }
    single { CLIReader(get()) }
    single { MD5HashingAlgorithm() }
    single { CreateMateUseCase(get(), get()) }
    single { MateCreationView(get(), get(), get()) }
    single { MatesManagementView(get(), get(), get(), get()) }
}