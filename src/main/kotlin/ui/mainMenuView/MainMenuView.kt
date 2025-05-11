package ui.mainMenuView

import logic.entities.User
import logic.useCases.LogoutUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.matesManagementView.MatesManagementView
import ui.projectsDashboardView.ProjectsDashboardView

class MainMenuView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectsDashboardView: ProjectsDashboardView,
    private val matesManagementView: MatesManagementView,
    private val logoutUseCase: LogoutUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private lateinit var loggedInUserType: User.Type
    private var currentViewState: ViewState<Unit> = ViewState.Loading

    suspend fun start(loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType
        while (true) {
            printMainMenuTitle()
            printOptions()

            when (getValidUserInput()) {
                1 -> projectsDashboardView.start(loggedInUserType)
                2 -> {
                    if (loggedInUserType == User.Type.ADMIN) {
                        matesManagementView.start()
                    } else {
                        printLn("Access denied. Only admins can manage mates.")
                    }
                }

                0 -> {
                    performLogout()
                    break
                }

                else -> printLn("Invalid input, please try again.")
            }
        }
    }

    private fun printMainMenuTitle() {
        cliPrinter.printHeader("Main Menu")
    }

    private fun printOptions() {
        printLn("1. View all projects")
        if (loggedInUserType == User.Type.ADMIN) printLn("2. Mates management")
        printLn("0. Logout")
    }

    private suspend fun performLogout() {
        printLn("\nLogging out...")

        viewExceptionHandler.executeWithState(
            onLoading = {
                currentViewState = ViewState.Loading
            },
            onSuccess = {
                currentViewState = ViewState.Success(Unit)
                printLn("Logout successful.")
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                printLn("Logout failed: ${exception.message}")
            },
            operation = {
                logoutUseCase()
            }
        )
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            if (loggedInUserType == User.Type.ADMIN) MAX_OPTION_NUMBER_ADMIN
            else MAX_OPTION_NUMBER_MATE

        return cliReader.getValidInputNumberInRange(max = maxOptionNumberAllowed)
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 2
        const val MAX_OPTION_NUMBER_MATE = 1
    }
}