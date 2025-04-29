package ui.mainMenuView

import logic.entities.User
import logic.useCases.getUserTypeUseCase.GetUserTypeUseCase
import ui.CLIPrintersAndReaders.CLIPrinter
import ui.CLIPrintersAndReaders.CLIReader
import ui.View
import ui.loginView.LoginView
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView

class MainMenuView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getUserTypeUseCase: GetUserTypeUseCase,
    private val loginView: LoginView,
    private val projectsView: ProjectsView,
    private val matesManagementView: MatesManagementView
) : View {

    private var userType: User.Type? = null

    override fun start() {

        saveUserType()
        printMainMenuTitle()

        if (userType == null) {
            cliPrinter.printPleaseLoginMessage()
            loginView.start()
            return
        }

        printOptions()
        goToNextUI()
    }

    private fun saveUserType() {
        userType = getUserTypeUseCase.getUserType()
    }

    private fun printMainMenuTitle() {
        cliPrinter.printHeader("Main Menu")
    }

    private fun printOptions() {
        printLn("1. View all project")
        if (userType == User.Type.ADMIN) printLn("2. Mates management")
        printLn("0. view all project")
    }

    private fun goToNextUI() {
        when (getValidUser()) {
            "1" -> projectsView.start()
            "2" -> matesManagementView.start()
            "0" -> loginView.start()
        }
    }

    private fun getValidUser(): String {
        val validInputs = listOf("0", "1", "2").takeIf { userType == User.Type.ADMIN } ?: listOf("0", "1")
        val userInput = cliReader.getUserInput("choose an option").trim()
        if (userInput in validInputs) return userInput
        cliPrinter.cliPrintLn("invalid option, try again ...")
        return getValidUser()

    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}