package ui.mainMenuView

import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.useCases.GetLoggedInUserUseCase
import logic.useCases.LogoutUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.matesManagementView.MatesManagementView
import ui.projectDetailsView.ProjectDetailsView
import ui.projectsDashboardView.ProjectsDashboardView

class MainMenuView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val projectDetailsView: ProjectsDashboardView,
    private val matesManagementView: MatesManagementView,
    private val logoutUseCase: LogoutUseCase,
) {

    private lateinit var loggedInUserType: User.Type

    fun start() {
        printMainMenuTitle()
        if (!saveUserType()) return
        printOptions()
        goToNextUI()
    }

    private fun saveUserType(): Boolean {
        return try {
            loggedInUserType = getLoggedInUserUseCase.getLoggedInUser().type
            true
        } catch (e: NoLoggedInUserIsSavedInCacheException) {
            printLn("please login to continue")
            false
        }
    }

    private fun printMainMenuTitle() {
        cliPrinter.printHeader("Main Menu")
    }

    private fun printOptions() {
        printLn("1. View all project")
        if (loggedInUserType == User.Type.ADMIN) printLn("2. Mates management")
        printLn("0. Logout")
    }

    private fun goToNextUI() {
        when (getValidUserInput()) {
            "1" -> projectDetailsView.start()
            "2" -> matesManagementView.start()
            "0" -> {
                printLn("\nLogging out ...")
                logoutUseCase()
                return // exit main menu
            }
        }
        start() // start main menu again after going back from options
    }

    private fun getValidUserInput(): String {
        val MAX_OPTION_NUMBER = MAX_OPTION_NUMBER_ADMIN.takeIf { loggedInUserType == User.Type.ADMIN } ?: MAX_OPTION_NUMBER_MATE
        return cliReader.getValidUserNumberInRange(MAX_OPTION_NUMBER)
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 2
        const val MAX_OPTION_NUMBER_MATE = 1
    }
}