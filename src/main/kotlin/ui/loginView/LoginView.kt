package ui.loginView

import logic.exceptions.AuthenticationException
import logic.useCases.LoginUseCase
import logic.useCases.SetLoggedInUserUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.mainMenuView.MainMenuView

class LoginView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginUseCase: LoginUseCase,
    private val mainMenuView: MainMenuView,
    private val setLoggedInUserUseCase: SetLoggedInUserUseCase
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
            val user = loginUseCase(username, password)
            setLoggedInUserUseCase.setLoggedInUser(user)
            println("Login successful")
            mainMenuView.start()
        } catch (e: AuthenticationException) {
            println(e.message)
        } catch (e: Exception) {
            println("Invalid username or password")
        }
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}