package ui.matesManagementView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import logic.entities.User
import logic.useCases.mateUseCase.GetAllMatesUseCase
import logic.useCases.GetLoggedInUserUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter

class MatesManagementView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val getAllMatesUseCase: GetAllMatesUseCase,
    private val cliTablePrinter: CLITablePrinter,
    private val mateCreationView: MateCreationView
) {

    fun start() {
        var currentUser: User? = null
        CoroutineScope(Dispatchers.Main).launch {
            currentUser = getLoggedInUserUseCase.getLoggedInUser()
        }
        if (currentUser?.type != User.Type.ADMIN) {
            printLn("Error: Only admins can manage mates.")
            return
        }
        printOptions()
        selectNextUI()
    }

    private fun printOptions() {
        printLn("Mates Management Menu")
        listAllMates()
        printLn("1. Create New Mate")
        printLn("0. Back")
    }

    private fun selectNextUI() {
        val userInput = cliReader.getValidInputNumberInRange(MAX_OPTION_NUMBER)

        when (userInput) {
            1 -> mateCreationView.createMate()
            0 -> return
        }
        start()
    }

    private fun listAllMates() {
        var mates: List<User> = emptyList()
        CoroutineScope(Dispatchers.Main).launch {
            mates = getAllMatesUseCase.getAllMates()
        }
        if (mates.isEmpty()) {
            printLn("No mates found.")
            return
        }

        val headers = listOf("ID", "Username", "Type")
        val data = mates.map { mate ->
            listOf(mate.id.toString(), mate.userName, mate.type.toString())
        }
        val columnWidths = listOf(null, null, null)

        cliTablePrinter(headers, data, columnWidths)
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 1
    }
}