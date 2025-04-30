package ui.welcomeView

import logic.repositories.CacheDataRepository
import ui.cliPrintersAndReaders.CLIPrinter
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView

class WelcomeView(
    private val cliPrinter: CLIPrinter,
    private val loginView: LoginView,
    private val mainMenuView: MainMenuView,
    private val cacheDataRepository: CacheDataRepository,
    ){

    fun start() {
        val loggedInUser = cacheDataRepository.getLoggedInUser() ?: run{
            cliPrinter.printPleaseLoginMessage()
            loginView.start()
            return
        }

        printWelcomeMessage(loggedInUser.userName)
        mainMenuView.start()
    }

    private fun printWelcomeMessage(userName: String) {
        cliPrinter.cliPrintLn("welcome $userName to PlanMate V1.0")
    }
}