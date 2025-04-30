package ui.loginView

import logic.entities.User
import logic.usecases.loginUseCase.LoginUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class LoginView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginUseCase: LoginUseCase
) {

    fun start() {
        printLoginTitle()

        val username = readUsername()
        if (username.isBlank()) {
            handleEmptyUsername()
            return
        }

        val password = readPassword()
        if (password.isBlank()) {
            handleEmptyPassword()
            return
        }

        processLogin(username, password)
    }

    private fun printLoginTitle() {
        cliPrinter.printHeader("Login")
        cliPrinter.cliPrintLn("Please enter your username and password.")
    }

    private fun readUsername(): String {
        return cliReader.getUserInput("username: ")
    }

    private fun readPassword(): String {
        return cliReader.getUserInput("password: ")
    }

    private fun handleEmptyUsername() {
        cliPrinter.cliPrintLn("Username is empty. Please try again.")
    }

    private fun handleEmptyPassword() {
        cliPrinter.cliPrintLn("Password is empty. Please try again.")
    }

    private fun processLogin(username: String, password: String) {
        val user = loginUseCase.login(username, password)
        if (user != null) {
            handleSuccessfulLogin(user)
        } else {
            handleInvalidCredentials()
        }
    }

    private fun handleSuccessfulLogin(user: User) {
        cliPrinter.cliPrintLn("Login successful")
    }

    private fun handleInvalidCredentials() {
        cliPrinter.cliPrintLn("Invalid username or password")
    }
}