package ui.matesManagementView

import logic.useCases.CreateMateUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class MateCreationView(
    private val createMateUseCase: CreateMateUseCase,
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
) : RequestHandler(cliPrinter) {
    fun createMate() {
        cliPrinter.printHeader("Create New Mate")

        val (username, password) = getUserCredentials()

        makeRequest(
            request = { createMateUseCase.createMate(username, password) },
            onSuccess = { cliPrinter.cliPrintLn("Mate ($username) have been created successfully") },
            onLoadingMessage = "Creating mate..."
        )
    }

    private fun getUserCredentials(): Pair<String, String> {
        val username = cliReader.getValidUserInput(
            { it.isNotBlank() },
            "Enter username: ",
            "username can not be empty"
        )
        val password = cliReader.getValidUserInput(
            { it.isNotBlank() },
            "Enter password: ",
            "password can not be empty"
        )
        return Pair(username, password)
    }
}