package ui.mainMenuView

import logic.entities.User
import logic.useCases.LogoutUseCase
import ui.RequestHandler
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
) : RequestHandler(cliPrinter) {

    private lateinit var loggedInUserType: User.Type

    fun start(loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType
        printMainMenuTitle()
        printOptions()
        goToNextView()
    }

    private fun printMainMenuTitle() {
        cliPrinter.printHeader("Main Menu")
    }

    private fun printOptions() {
        cliPrinter.cliPrintLn("1. View all project")
        if (loggedInUserType == User.Type.ADMIN) cliPrinter.cliPrintLn("2. Mates management")
        cliPrinter.cliPrintLn("0. Logout")
    }

    private fun goToNextView() {
        when (getValidUserInput()) {
            1 -> goToProjectsDashboard()
            2 -> goToMatesManagement()
            0 -> makeRequest(
                request = { logoutUseCase() },
                onSuccess = { cliPrinter.cliPrintLn("Logout successful") },
                onLoadingMessage = "Logging out..."
            )
        }
    }

    private fun goToProjectsDashboard() {
        projectsDashboardView.start(loggedInUserType)
        start(loggedInUserType)
    }

    private fun goToMatesManagement() {
        matesManagementView.start()
        start(loggedInUserType)
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            MAX_OPTION_NUMBER_ADMIN.takeIf { loggedInUserType == User.Type.ADMIN } ?: MAX_OPTION_NUMBER_MATE
        return cliReader.getValidInputNumberInRange(maxOptionNumberAllowed)
    }

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 2
        const val MAX_OPTION_NUMBER_MATE = 1
    }
}