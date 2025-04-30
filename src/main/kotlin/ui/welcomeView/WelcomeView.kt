package ui.welcomeView

import logic.useCases.GetActiveUserUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.mainMenuView.MainMenuView

class WelcomeView(
    private val cliPrinter: CLIPrinter,
    private val mainMenuView: MainMenuView,
    private val getActiveUserUseCase: GetActiveUserUseCase
){

    fun start() {
        val loggedInUser = getActiveUserUseCase.getLoggedInUser() ?: run{
            cliPrinter.printPleaseLoginMessage()
            return
        }

        printWelcomeMessage(loggedInUser.userName)
        mainMenuView.start()
    }

    private fun printWelcomeMessage(userName: String) {
        cliPrinter.cliPrintLn("welcome $userName to PlanMate V1.0")
    }
}