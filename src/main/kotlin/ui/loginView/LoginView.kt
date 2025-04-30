package ui.loginView

import logic.repositories.CacheDataRepository
import logic.useCases.loginUseCase.LoginUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.mainMenuView.MainMenuView

class LoginView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginUseCase: LoginUseCase,
    private val mainMenuView: MainMenuView,
    private val cacheDataRepository: CacheDataRepository,
) {

    fun startLogin() {
        printLoginTitle()

        val username = cliReader.getValidUserInput({ it.isNotBlank() && it.length < 30 }, "username: ")
        val password = cliReader.getValidUserInput({ it.isNotBlank() && it.length < 30 }, "password: ")

        processLogin(username, password)
    }

    private fun printLoginTitle() {
        println("Login")
        println("Please enter your username and password\n")
    }

    private fun processLogin(username: String, password: String) {
        val user = loginUseCase.login(username, password)
        if (user != null) {
            cacheDataRepository.setLoggedInUser(user)
            println("Login successful")
            mainMenuView.startMainMenu()
        } else {
            println("Invalid username or password")
        }
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}