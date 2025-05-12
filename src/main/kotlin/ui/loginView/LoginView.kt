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
            "username can not be empty"
        )
        val password = cliReader.getValidUserInput(
            { it.isNotBlank() },
            "Enter password: ",
            "password can not be empty"
        )
        return Pair(username, password)
    }

    private fun processLogin(username: String, password: String) {
        var loggedInUserType = User.Type.MATE

        makeRequest(
            request = { loggedInUserType = loginUseCase(username, password).type },
            onSuccess = {
                cliPrinter.cliPrintLn("Login successful")
                mainMenuView.start(loggedInUserType)
            },
            onLoadingMessage = "Logging in..."
        )
    }
}