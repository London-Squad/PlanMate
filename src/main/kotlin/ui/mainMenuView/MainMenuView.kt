package ui.mainMenuView

import logic.entities.User
import logic.repositories.CatchDataRepository
import ui.CLIPrintersAndReaders.CLIPrinter
import ui.CLIPrintersAndReaders.CLIReader
import ui.View
import ui.loginView.LoginView
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView

class MainMenuView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val catchDataRepository: CatchDataRepository,
    private val loginView: LoginView,
    private val projectsView: ProjectsView,
    private val matesManagementView: MatesManagementView
) : View {

    private var userActiveType: User.Type? = null

    override fun start() {

        saveUserType()
        printMainMenuTitle()

        if (userActiveType == null) {
            cliPrinter.printPleaseLoginMessage()
            loginView.start()
            return
        }

        printOptions()
        goToNextUI()
    }

    private fun saveUserType() {
        userActiveType = catchDataRepository.getLoggedInUser()?.type
    }

    private fun printMainMenuTitle() {
        cliPrinter.printHeader("Main Menu")
    }

    private fun printOptions() {
        printLn("1. View all project")
        if (userActiveType == User.Type.ADMIN) printLn("2. Mates management")
        printLn("0. Logout")
    }

    private fun goToNextUI() {
        when (getValidUserInput()) {
            "1" -> projectsView.start()
            "2" -> matesManagementView.start()
            "0" -> loginView.start()
        }
    }

    private fun getValidUserInput(): String {
        val validInputs = listOf("0", "1", "2").takeIf { userActiveType == User.Type.ADMIN } ?: listOf("0", "1")
        val userInput = cliReader.getUserInput("choose an option").trim()
        if (userInput in validInputs) return userInput
        cliPrinter.cliPrintLn("invalid option, try again ...")
        return getValidUserInput()

    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}