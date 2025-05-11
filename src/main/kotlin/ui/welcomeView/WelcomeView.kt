package ui.welcomeView

import logic.entities.User
import logic.exceptions.NoLoggedInUserFoundException
import logic.useCases.GetLoggedInUserUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView

class WelcomeView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginView: LoginView,
    private val mainMenuView: MainMenuView,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val viewExceptionHandler: ViewExceptionHandler,
) {

    private var currentState: ViewState<User> = ViewState.Loading

    suspend fun start() {
        checkForLoggedInUser()
    }

    private suspend fun checkForLoggedInUser() {
        viewExceptionHandler.executeWithState(
            onLoading = {
                cliPrinter.cliPrintLn("Checking for logged in user...")
                currentState = ViewState.Loading
            },
            onSuccess = { user ->
                currentState = ViewState.Success(user)
                mainMenuView.start(user.type)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                if (exception is NoLoggedInUserFoundException) {
                    startNormalWelcomeView()
                } else {
                    cliPrinter.cliPrintLn("Error: ${exception.message}")
                    startNormalWelcomeView()
                }
            },
            operation = {
                getLoggedInUserUseCase.getLoggedInUser()
            }
        )
    }

    private suspend fun startNormalWelcomeView() {
        printWelcomeMessage()
        printOptions()
        goToNextView()
    }

    private fun printWelcomeMessage() {
        cliPrinter.printHeader("Welcome to PlanMate")
    }

    private fun printOptions() {
        cliPrinter.cliPrintLn("1. Login")
        cliPrinter.cliPrintLn("0. Exit the app")
    }

    private suspend fun goToNextView() {
        when (cliReader.getValidInputNumberInRange(max = MAX_OPTION_NUMBER)) {
            1 -> {
                loginView.start()
                start()
            }

            0 -> {
                cliPrinter.cliPrintLn("Exiting the app...")
            }
        }
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 1
    }
}
