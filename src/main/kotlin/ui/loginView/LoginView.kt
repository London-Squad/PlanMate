package ui.loginView

import logic.exceptions.AuthenticationException
import logic.useCases.LoginUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.mainMenuView.MainMenuView

class LoginView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginUseCase: LoginUseCase,
    private val mainMenuView: MainMenuView,
) {

    fun start() {
        cliPrinter.printHeader("Login")
        println("Please enter your username and password\n")

        val username = cliReader.getUserInput("username: ")
        val password = cliReader.getUserInput("password: ")

        processLogin(username, password)
    }

    private fun processLogin(username: String, password: String) {
        try {
            loginUseCase(username, password)
            println("Login successful")
            mainMenuView.start()
        } catch (e: AuthenticationException) {
            println(e.message)
        } // catch (e: Exception) {
//            println("Invalid username or password. ${e.message}")
//        }
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}