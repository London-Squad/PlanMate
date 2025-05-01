package ui.mainMenuView

import logic.entities.User
import logic.repositories.CacheDataRepository
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView

class MainMenuView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val cacheDataRepository: CacheDataRepository,
    private val projectsView: ProjectsView,
    private val matesManagementView: MatesManagementView
) {

    private lateinit var loggedInUserType: User.Type

    fun start() {
        saveUserType()
        printMainMenuTitle()
        printOptions()
        goToNextUI()
    }

    private fun saveUserType() {
        loggedInUserType = cacheDataRepository.getLoggedInUser()!!.type
    }

    private fun printMainMenuTitle() {
        cliPrinter.printHeader("Main Menu")
    }

    private fun printOptions() {
        println("1. View all project")
        if (loggedInUserType == User.Type.ADMIN) println("2. Mates management")
        println("0. Logout")
    }

    private fun goToNextUI() {
        when (getValidUserInput()) {
            "1" -> projectsView.start()
            "2" -> matesManagementView.start()
            "0" -> {
                println("\nLogging out ...")
                cacheDataRepository.clearLoggedInUserFromCatch()
                return // exit main menu
            }
        }
        start() // start main menu again after going back from options
    }

    private fun getValidUserInput(): String {
        val validInputs = validInputsForAdmin.takeIf { loggedInUserType == User.Type.ADMIN } ?: validInputsForMate
        val userInput = cliReader.getUserInput("\nchoose an option: ").trim()
        if (userInput in validInputs) return userInput
        cliPrinter.cliPrintLn("invalid option, try again ...")
        return getValidUserInput()
    }

    private fun println(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        val validInputsForAdmin = listOf("0", "1", "2")
        val validInputsForMate = listOf("0", "1")
    }
}