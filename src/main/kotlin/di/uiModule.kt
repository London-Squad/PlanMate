package di

import org.koin.dsl.module
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView
import ui.taskManagementView.*
import ui.welcomeView.WelcomeView

val uiModule = module {
    single { CLIPrinter() }
    single { CLIReader(get()) }

    single { TaskTitleEditionView(get(), get(), get()) }
    single { TaskDescriptionEditionView(get(), get(), get()) }
    single { TaskStateEditionView(get(), get(), get()) }
    single { TaskDeletionView(get(), get(), get()) }
    single { TaskManagementView(get(), get(), get(), get(), get(), get()) }

    single { ProjectsView() }

    single { MatesManagementView() }

    single { MainMenuView(get(), get(), get(), get(), get()) }

    single { LoginView(get(), get(), get(), get(), get()) }

    single { WelcomeView(get(), get(), get()) }
}