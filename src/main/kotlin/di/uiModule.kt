package di

import org.koin.dsl.module
import ui.cliPrintersAndReaders.*
import ui.loginView.LoginView
import ui.logsView.LogsView
import ui.mainMenuView.MainMenuView
import ui.matesManagementView.MateCreationView
import ui.matesManagementView.MatesManagementView
import ui.projectDetailsView.DeleteProjectView
import ui.projectDetailsView.EditProjectView
import ui.projectDetailsView.ProjectDetailsView
import ui.projectDetailsView.SwimlanesView
import ui.projectsDashboardView.ProjectsDashboardView
import ui.taskManagementView.*
import ui.taskStatesView.TaskStatesView
import ui.welcomeView.WelcomeView

val uiModule = module {

    single { CLIPrinter() }
    single { CLIReader(get()) }
    single { ProjectInputReader(get(), get()) }
    single { TaskInputReader(get(), get()) }
    single { TaskStateInputReader(get(), get()) }
    single { CLITablePrinter(get()) }

    single { WelcomeView(get(), get(), get(), get(), get()) }
    single { LoginView(get(), get(), get(), get()) }
    single { MainMenuView(get(), get(), get(), get(), get()) }

    single { ProjectsDashboardView(get(), get(), get(), get(), get(), get(), get()) }
    single { ProjectDetailsView(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { SwimlanesView(get()) }
    single { EditProjectView(get(), get(), get(), get(), get()) }
    single { DeleteProjectView(get(), get(), get()) }
    single { TaskStatesView(get(), get(), get(), get(), get()) }

    single { TaskTitleEditionView(get(), get(), get()) }
    single { TaskDescriptionEditionView(get(), get(), get()) }
    single { TaskStateEditionView(get(), get(), get(), get()) }
    single { TaskDeletionView(get(), get(), get()) }
    single { TaskManagementView(get(), get(), get(), get(), get(), get(), get(), get(), get()) }

    single { LogsView(get(), get(), get(), get(), get()) }

    single { MateCreationView(get(), get(), get()) }
    single { MatesManagementView(get(), get(), get(), get(), get()) }
}