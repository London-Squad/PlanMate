package di

import org.koin.dsl.module
import ui.cLIPrintersAndReaders.CLIPrinter
import ui.cLIPrintersAndReaders.CLIReader

val appModule = module {
    single { CLIPrinter() }
    single { CLIReader(get()) }
}