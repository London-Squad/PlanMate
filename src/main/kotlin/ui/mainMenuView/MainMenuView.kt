package ui.mainMenuView

import logic.entities.User
import logic.useCases.LogoutUseCase
import ui.BaseView
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
    private val baseView: BaseView
) {

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
        printLn("1. View all project")
        if (loggedInUserType == User.Type.ADMIN) printLn("2. Mates management")
        printLn("0. Logout")
    }

    private fun goToNextView() {
        when (getValidUserInput()) {
            1 -> projectsDashboardView.start(loggedInUserType)
            2 -> matesManagementView.start()
            0 -> {
                printLn("\nLogging out ...")
                baseView.tryCall { logoutUseCase() }
                return
            }
        }
        start(loggedInUserType)
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            MAX_OPTION_NUMBER_ADMIN.takeIf { loggedInUserType == User.Type.ADMIN } ?: MAX_OPTION_NUMBER_MATE
        return cliReader.getValidInputNumberInRange(maxOptionNumberAllowed)
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 2
        const val MAX_OPTION_NUMBER_MATE = 1
    }
}