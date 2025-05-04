package di

import org.koin.dsl.module
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.projectView.ProjectView
import ui.loginView.LoginView
import ui.logsView.LogsView
import ui.mainMenuView.MainMenuView
import ui.matesManagementView.MateCreationView
import ui.matesManagementView.MatesManagementView
import ui.projectView.*
import ui.projectsView.ProjectsView
import ui.statesView.StatesView
import ui.taskManagementView.*
import ui.welcomeView.WelcomeView

val uiModule = module {

    single { CLIPrinter() }
    single { CLIReader(get(), get()) }

    single { TaskTitleEditionView(get(), get()) }
    single { TaskDescriptionEditionView(get(), get()) }
    single { TaskStateEditionView(get(), get(), get()) }
    single { TaskDeletionView(get(), get(), get()) }
    single { TaskManagementView(get(), get(), get(), get(), get(), get(), get(), get()) }

    single { ProjectTasksView(get(), get(), get(), get()) }
    single { LogsView(get(), get(), get()) }

    single { MainMenuView(get(), get(), get(), get(), get(), get()) }

    single { LoginView(get(), get(), get(), get(), get()) }

    single { WelcomeView(get(), get(), get(), get(), get()) }

    single { ProjectsView(get(), get(), get(), get(), get()) }

    single { ProjectView(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { SwimlanesView(get()) }
    single { StatesView(get(), get(), get()) }
    single { EditProjectView(get(), get(), get(), get()) }
    single { DeleteProjectView(get(), get()) }
    single { ProjectView(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { ProjectsView(get(), get(), get(), get(), get()) }

    single { MateCreationView(get(), get(), get()) }
    single { MatesManagementView(get(), get(), get(), get()) }

    single { MainMenuView(get(), get(), get(), get(), get(), get()) }

    single { LoginView(get(), get(), get(), get(), get()) }

    single { WelcomeView(get(), get(), get(), get(), get()) }
}