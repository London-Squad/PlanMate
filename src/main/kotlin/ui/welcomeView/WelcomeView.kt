package ui.welcomeView

import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView

class WelcomeView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginView: LoginView
){

    fun startWelcome() {

        cliPrinter.printHeader("Welcome to PlanMate v1.0")

        println("1. Login")
        println("0. Exit the app")
        if (cliReader.getValidUserInput({ it in listOf("1", "0") }, "\nenter your option: ") == "1") {
            loginView.startLogin()
            startWelcome()
        }
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}