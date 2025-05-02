package di

import data.security.hashing.MD5HashingAlgorithm
import org.koin.dsl.module
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

val appModule = module {
    single { CLIPrinter() }
    single { CLIReader(get()) }
    single { MD5HashingAlgorithm() }
}