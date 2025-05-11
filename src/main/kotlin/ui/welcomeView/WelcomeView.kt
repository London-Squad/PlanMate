package ui.welcomeView

import logic.exceptions.NoLoggedInUserFoundException
import logic.useCases.GetLoggedInUserUseCase
import ui.BaseView
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
) : BaseView(cliPrinter) {

    fun start() {
        tryCall(
            functionToTry = {
                val loggedInUserType = getLoggedInUserUseCase.getLoggedInUser().type
                mainMenuView.start(loggedInUserType)
                start()
            },
            onFailureFunction = { e: Exception ->
                if (e is NoLoggedInUserFoundException) {
                    startNormalWelcomeView()
                } else handleExceptionsInDefaultWay(e)
            }
        )
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