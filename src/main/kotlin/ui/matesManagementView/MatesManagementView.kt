package ui.matesManagementView

import logic.entities.User
import logic.useCases.GetAllMatesUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.CLITablePrinter

class MatesManagementView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getAllMatesUseCase: GetAllMatesUseCase,
    private val cliTablePrinter: CLITablePrinter,
    private val mateCreationView: MateCreationView
) : RequestHandler(cliPrinter) {

    fun start() {
        printOptions()
        selectNextUI()
    }

    private fun printOptions() {
        cliPrinter.cliPrintLn("Mates Management Menu")
        printTableOfAllMates()
        cliPrinter.cliPrintLn("1. Create New Mate")
        cliPrinter.cliPrintLn("0. Back")
    }

    private fun selectNextUI() {
        val userInput = cliReader.getValidInputNumberInRange(MAX_OPTION_NUMBER)

        when (userInput) {
            1 -> mateCreationView.createMate()
            0 -> return
        }
        start()
    }

    private fun printTableOfAllMates() {
        var mates: List<User> = emptyList()

        makeRequest(
            request = { mates = getAllMatesUseCase.getAllMates() },
            onLoadingMessage = "Fetching all mates..."
        )

        if (mates.isEmpty()) {
            cliPrinter.cliPrintLn("No mates found.")
            return
        }

        val headers = listOf("ID", "Username", "Type")
        val data = mates.map { mate ->
            listOf(mate.id.toString(), mate.userName, mate.type.toString())
        }
        val columnWidths = listOf(null, null, null)

        cliTablePrinter(headers, data, columnWidths)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 1
    }
}