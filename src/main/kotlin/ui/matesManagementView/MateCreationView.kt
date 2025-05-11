package ui.matesManagementView

import logic.useCases.mateUseCase.CreateMateUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class MateCreationView(
    private val createMateUseCase: CreateMateUseCase,
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val viewExceptionHandler: ViewExceptionHandler
)  {
    private var currentViewState: ViewState<Unit> = ViewState.Loading

    suspend fun createMate(onComplete: suspend () -> Unit = {}) {
        cliPrinter.printHeader("Create New Mate")

        val username = cliReader.getValidUserInput(
            { it.isNotBlank() },
            "Enter username: ",
            "Username can't be empty"
        )

        val password = cliReader.getValidUserInput(
            { it.isNotBlank() },
            "Enter password: ",
            "Password can't be empty"
        )

        performMateCreation(username, password, onComplete)
    }

    private suspend fun performMateCreation(username: String, password: String, onComplete: suspend () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Creating mate...")
                currentViewState = ViewState.Loading
            },
            onSuccess = {
                currentViewState = ViewState.Success(Unit)
                printLn("Mate created successfully: $username")
                onComplete()
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                printLn("Failed to create mate: ${exception.message}")
                onComplete()
            },
            operation = {
                createMateUseCase.createMate(username, password)
            }
        )
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}