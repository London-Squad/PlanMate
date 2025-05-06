package ui.loginView

import logic.entities.User
import logic.useCases.LoginUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.mainMenuView.MainMenuView

class LoginView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginUseCase: LoginUseCase,
    private val mainMenuView: MainMenuView,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    fun start() {
        cliPrinter.printHeader("Login")
        println("Please enter your username and password\n")

        val username = cliReader.getUserInput("username: ")
        val password = cliReader.getUserInput("password: ")

        processLogin(username, password)
    }

    private fun processLogin(username: String, password: String) {
        var loggedInUserType = User.Type.MATE

        viewExceptionHandler.tryCall {
            loggedInUserType = loginUseCase(username, password).type
        }.also { if (!it) return }

        println("Login successful")
        mainMenuView.start(loggedInUserType)
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}