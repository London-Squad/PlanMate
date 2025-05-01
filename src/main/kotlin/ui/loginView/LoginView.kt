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
        cliPrinter.printHeader("Login")
        println("Please enter your username and password\n")

        val username = cliReader.getValidUserInput({ it.isNotBlank() && it.length < 30 }, "username: ")
        val password = cliReader.getValidUserInput({ it.isNotBlank() && it.length < 30 }, "password: ")

        processLogin(username, password)
    }

    private fun processLogin(username: String, password: String) {
        try {
            val user = loginUseCase.login(username, password)
            cacheDataRepository.setLoggedInUser(user)
            println("Login successful")
            mainMenuView.startMainMenu()
        } catch (_: Exception) {
            println("Invalid username or password")
        }
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}