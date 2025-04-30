package ui.loginView

import logic.usecases.loginUseCase.LoginUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class LoginView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginUseCase: LoginUseCase
) {

    fun start() {
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
}