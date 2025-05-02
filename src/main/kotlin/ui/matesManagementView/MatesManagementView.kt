package ui.matesManagementView

import logic.entities.User
import logic.repositories.CacheDataRepository
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class MatesManagementView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val cacheDataRepository: CacheDataRepository,
    private val mateCreationView: MateCreationView
) {

    fun start() {
        val currentUser = cacheDataRepository.getLoggedInUser()
        if (currentUser == null || currentUser.type != User.Type.ADMIN) {
            printLn("Error: Only admins can manage mates.")
            return
        }
        printOptions()
        selectNextUI()
    }

    private fun printOptions() {
        printLn("Mates Management Menu")
        printLn("1. Create New Mate")
        printLn("0. Back")
    }

    private fun selectNextUI() {
        when (getValidUserInput()) {
            "1" -> mateCreationView.createMate()
            "0" -> return
        }
        start()
    }

    private fun getValidUserInput(): String {
        val userInput = cliReader.getUserInput("Your option: ")
        if (userInput in "0"..MAX_OPTION) return userInput
        else {
            printLn("Invalid option")
            return getValidUserInput()
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION = "1"
    }

}