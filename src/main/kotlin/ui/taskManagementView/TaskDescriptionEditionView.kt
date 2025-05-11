package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.UUID

class TaskDescriptionEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private var currentViewState: ViewState<Unit> = ViewState.Loading

    suspend fun editDescription(taskId: UUID, onComplete: suspend () -> Unit = {}) {
        val newDescription = taskInputReader.getValidTaskDescription()
        updateTaskDescription(taskId, newDescription, onComplete)
    }

    private suspend fun updateTaskDescription(taskId: UUID, newDescription: String, onComplete: suspend () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                currentViewState = ViewState.Loading
            },
            onSuccess = {
                currentViewState = ViewState.Success(Unit)
                onComplete()
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                onComplete()
            },
            operation = {
                manageTaskUseCase.editTaskDescription(taskId, newDescription)
            }
        )
    }
}