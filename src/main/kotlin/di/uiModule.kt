package di

import org.koin.dsl.module
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.welcomeView.WelcomeView

val uiModule = module {
    single { CLIPrinter() }
    single { CLIReader(get()) }
    single { LoginView() }
    single { WelcomeView(get(), get(), get()) }
}