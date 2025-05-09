package ui.welcomeView

import logic.planeMateException.NoLoggedInUserFoundException
import logic.useCases.GetLoggedInUserUseCase
import ui.ViewExceptionHandler
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
    private val viewExceptionHandler: ViewExceptionHandler
) {

    fun start() {
        viewExceptionHandler.tryCall {
            try { // try to skip login if user is already logged in
                val loggedInUserType = getLoggedInUserUseCase.getLoggedInUser().type
                mainMenuView.start(loggedInUserType)
                start()
            } catch (_: NoLoggedInUserFoundException) {
                startNormalWelcomeView()
            }
        }
    }

    private fun startNormalWelcomeView() {
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

    private fun goToNextView() {
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