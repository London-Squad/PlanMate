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

    fun start() {
        cliPrinter.printHeader("Login")
        println("Please enter your username and password\n")

        val username = cliReader.getUserInput("username: ")
        val password = cliReader.getUserInput("password: ")

        processLogin(username, password)
    }

    private fun processLogin(username: String, password: String) {
        try {
            val user = loginUseCase.login(username, password)
            cacheDataRepository.setLoggedInUser(user)
            println("Login successful")
            mainMenuView.start()
        } catch (_: Exception) {
            println("Invalid username or password")
        }
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}