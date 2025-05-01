package di

import org.koin.dsl.module
import ui.projectView.ProjectView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView
import ui.welcomeView.WelcomeView

val uiModule = module {
    single { ProjectsView(get(), get(), get(), get(), get()) }
    single { ProjectView(get(), get(), get(), get()) }
    single { CLIPrinter() }
    single { CLIReader(get()) }

    single { ProjectsView() }

    single { MatesManagementView() }

    single { MainMenuView(get(), get(), get(), get(), get()) }

    single { LoginView(get(), get(), get(), get(), get()) }

    single { WelcomeView(get(), get(), get()) }
}