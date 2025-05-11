package ui.loginView

import logic.entities.User
import logic.useCases.LoginUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.mainMenuView.MainMenuView

class LoginView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginUseCase: LoginUseCase,
    private val mainMenuView: MainMenuView,
) : BaseView(cliPrinter) {

    fun start() {
        cliPrinter.printHeader("Login")
        cliPrinter.cliPrintLn("Please enter your username and password\n")

        val (username, password) = getUserCredentials()

        processLogin(username, password)
    }

    private fun getUserCredentials(): Pair<String, String> {
        val username = cliReader.getValidUserInput(
            { it.isNotBlank() },
            "Enter username: ",
            "username cant be empty"
        )
        val password = cliReader.getValidUserInput(
            { it.isNotBlank() },
            "Enter password: ",
            "password cant be empty"
        )
        return Pair(username, password)
    }

    private fun processLogin(username: String, password: String) {
        var loggedInUserType = User.Type.MATE

        tryCall({
            loggedInUserType = loginUseCase(username, password).type
        }).also { success -> if (!success) return }

        cliPrinter.cliPrintLn("Login successful")
        mainMenuView.start(loggedInUserType)
    }
}