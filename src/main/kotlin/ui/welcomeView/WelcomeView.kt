package ui.welcomeView

import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.useCases.GetLoggedInUserUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView

class WelcomeView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginView: LoginView,
    private val mainMenuView: MainMenuView,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) {

    fun start() {
        try {
            getLoggedInUserUseCase.getLoggedInUser()
            mainMenuView.start()
            start()
            return
        } catch (_: NoLoggedInUserIsSavedInCacheException) { }

        printWelcomeMessage()
        printOptions()
        goToNextView()
    }

    private fun printWelcomeMessage() {
        cliPrinter.printHeader("Welcome to PlanMate v1.0")
    }

    private fun printOptions() {
        println("1. Login")
        println("0. Exit the app")
    }

    private fun goToNextView() {
        when (cliReader.getValidUserNumberInRange(MAX_OPTION_NUMBER)) {
            "1" -> loginView.start()
            "0" -> {
                println("Exiting the app...")
                return
            }
        }
        start()
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER = 1
    }
}