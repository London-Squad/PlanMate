package ui.loginView

import logic.entities.User
import logic.useCases.LoginUseCase
import ui.ViewExceptionHandler
import ui.ViewState
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
    private var currentState: ViewState<User> = ViewState.Loading

    suspend fun start() {
        cliPrinter.printHeader("Login")
        println("Please enter your username and password\n")

        val username = cliReader.getUserInput("username: ")
        val password = cliReader.getUserInput("password: ")

        processLogin(username, password)
    }

    private suspend fun processLogin(username: String, password: String) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                println("Logging in...")
                currentState = ViewState.Loading
            },
            onSuccess = { user ->
                currentState = ViewState.Success(user)
                println("Login successful!")
                mainMenuView.start(user.type)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                println("Login failed: ${exception.message}")
            },
            operation = {
                loginUseCase(username, password)
            }
        )
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}
