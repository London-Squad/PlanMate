package ui.projectDetailsView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
)  {
    private var currentState: ViewState<Unit> = ViewState.Loading

    suspend fun deleteProject(projectId: UUID, onComplete: () -> Unit = {}) {
        if (isDeletionCanceled()) {
            printLn("Project deletion canceled")
            onComplete()
            return
        }

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Deleting project...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                currentState = ViewState.Success(Unit)
                printLn("Project deleted successfully.")
                onComplete()
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to delete project: ${exception.message}")
                onComplete()
            },
            operation = {
                manageProjectUseCase.deleteProject(projectId)
            }
        )
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}