package ui.welcomeView

import logic.entities.User
import logic.exceptions.NoLoggedInUserFoundException
import logic.useCases.GetLoggedInUserUseCase
import ui.RequestHandler
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
) : RequestHandler(cliPrinter) {

     suspend fun start() {
        var loggedInUserType = User.Type.MATE

        makeRequest(
            request = { loggedInUserType = getLoggedInUserUseCase.getLoggedInUser().type },
            onSuccess = { goDirectlyToMainMenu(loggedInUserType) },
//            onError = (::onCheckingLoggedInUserFailure),
            onLoadingMessage = "Checking for logged in user..."
        )
    }

    private suspend fun goDirectlyToMainMenu(loggedInUserType: User.Type) {
        cliPrinter.cliPrintLn("Logged in user found, redirecting to main menu...")
        mainMenuView.start(loggedInUserType)
        start()
    }

    private suspend fun onCheckingLoggedInUserFailure(exception: Exception) {
        if (exception is NoLoggedInUserFoundException) startNormalWelcomeView()
        else handleDefaultExceptions(exception)
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
        when (cliReader.getValidInputNumberInRange(MAX_OPTION_NUMBER)) {
            1 -> loginView.start()
            0 -> {
                cliPrinter.cliPrintLn("Exiting the app...")
                return
            }
        }
        start()
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 1
    }
}