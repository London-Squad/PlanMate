package ui.welcomeView

import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView

class WelcomeView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val loginView: LoginView
) {

    fun start() {
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
        when (getUserInput()) {
            "1" -> loginView.start()
            "0" -> {
                println("Exiting the app...")
                return
            }
        }
        start() // start the welcome view again after coming back from options
    }

    private fun getUserInput() = cliReader.getValidUserInput(
        { it in listOf("1", "0") },
        "\nenter your option: "
    )

    private fun println(message: String) = cliPrinter.cliPrintLn(message)
}