package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.UUID

class TaskTitleEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler,
    private val cliPrinter: CLIPrinter
) {
    private var currentState: ViewState<Unit> = ViewState.Success(Unit)

    suspend fun editTitle(taskId: UUID, onComplete: suspend () -> Unit = {}) {
        val newTitle = taskInputReader.getValidTaskTitle()

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Updating task title...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                printLn("Task title updated successfully.")
                currentState = ViewState.Success(Unit)
                onComplete()
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to update task title: ${exception.message}")
            },
            operation = {
                manageTaskUseCase.editTaskTitle(taskId, newTitle)
            }
        )
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)
}