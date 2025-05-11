package ui.taskManagementView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class TaskStateEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val manageStateUseCase: ManageStateUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private var currentState: ViewState<List<TaskState>> = ViewState.Loading

    suspend fun editState(taskId: UUID, projectId: UUID, onComplete: suspend () -> Unit = {}) {
        loadProjectStates(taskId, projectId, onComplete)
    }

    private suspend fun loadProjectStates(taskId: UUID, projectId: UUID, onComplete: suspend () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading available task states...")
                currentState = ViewState.Loading
            },
            onSuccess = { projectTasksStates ->
                currentState = ViewState.Success(projectTasksStates)

                if (projectTasksStates.isEmpty()) {
                    printLn("No states available for this project.")
                    onComplete()
                    return@executeWithState
                }

                processStateSelection(taskId, projectTasksStates, onComplete)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to load task states: ${exception.message}")
                onComplete()
            },
            operation = {
                manageStateUseCase.getTaskStatesByProjectId(projectId)
            }
        )
    }

    private suspend fun processStateSelection(
        taskId: UUID,
        projectTasksStates: List<TaskState>,
        onComplete: suspend () -> Unit
    ) {
        printLn("Select a new state for the task:")
        printProjectState(projectTasksStates)

        val newStateIndex = cliReader.getValidInputNumberInRange(
            min = 1,
            max = projectTasksStates.size
        ) - 1

        updateTaskState(taskId, projectTasksStates[newStateIndex].id, onComplete)
    }

    private suspend fun updateTaskState(taskId: UUID, newStateId: UUID, onComplete: suspend () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Updating task state...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                printLn("Task state updated successfully.")
                currentState = ViewState.Success(emptyList())
                onComplete()
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to update task state: ${exception.message}")
                onComplete()
            },
            operation = {
                manageTaskUseCase.editTaskState(taskId, newStateId)
            }
        )
    }

    private fun printProjectState(tasksStates: List<TaskState>) {
        tasksStates.forEachIndexed { index, state ->
            printLn("${index + 1}. ${state.title}")
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}