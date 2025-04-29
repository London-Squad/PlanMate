package di

import org.koin.dsl.module
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

val appModule = module {
    single { CLIPrinter() }
    single { CLIReader(get()) }
}