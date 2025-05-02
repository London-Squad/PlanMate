package ui.mainMenuView

import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.useCases.ClearLoggedInUserFromCacheUseCase
import logic.useCases.GetLoggedInUserUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView

class MainMenuView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val clearLoggedInUserFromCacheUseCase: ClearLoggedInUserFromCacheUseCase,
    private val projectsView: ProjectsView,
    private val matesManagementView: MatesManagementView
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
            "1" -> projectsView.start()
            "2" -> matesManagementView.start()
            "0" -> {
                printLn("\nLogging out ...")
                clearLoggedInUserFromCacheUseCase.clearLoggedInUserFromCache()
                return // exit main menu
            }
        }
        start() // start main menu again after going back from options
    }

    private fun getValidUserInput(): String {
        val validInputs = validInputsForAdmin.takeIf { loggedInUserType == User.Type.ADMIN } ?: validInputsForMate
        val userInput = cliReader.getUserInput("\nchoose an option: ").trim()
        if (userInput in validInputs) return userInput
        cliPrinter.cliPrintLn("invalid option, try again ...")
        return getValidUserInput()
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        val validInputsForAdmin = listOf("0", "1", "2")
        val validInputsForMate = listOf("0", "1")
    }
}