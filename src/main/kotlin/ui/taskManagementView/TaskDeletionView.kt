package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class TaskDeletionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private var currentViewState: ViewState<Unit> = ViewState.Loading

    suspend fun deleteTask(taskId: UUID, onComplete: () -> Unit = {}) {
        if (isDeletionCanceled()) {
            printLn("Deletion canceled")
            onComplete()
            return
        }

        performTaskDeletion(taskId, onComplete)
    }

    private suspend fun performTaskDeletion(taskId: UUID, onComplete: () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Deleting task...")
                currentViewState = ViewState.Loading
            },
            onSuccess = {
                currentViewState = ViewState.Success(Unit)
                printLn("Task $taskId was deleted successfully")
                onComplete()
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                printLn("Failed to delete task: ${exception.message}")
                onComplete()
            },
            operation = {
                manageTaskUseCase.deleteTask(taskId)
            }
        )
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}