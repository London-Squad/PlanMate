package ui.taskStatesView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.TaskStateInputReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import java.util.*
import kotlin.coroutines.CoroutineContext

class TaskStatesView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val taskStateInputReader: TaskStateInputReader,
    private val useCase: ManageStateUseCase,
    private val viewExceptionHandler: ViewExceptionHandler,
    private val cliTablePrinter: CLITablePrinter
) {
    private lateinit var projectId: UUID
    private var tasksStates: List<TaskState> = emptyList()
    private var currentState: ViewState<List<TaskState>> = ViewState.Loading

    suspend fun start(projectId: UUID) {
        this.projectId = projectId
        cliPrinter.printHeader("Task States Management")
        getTaskStates()
    }

    private suspend fun showMainMenu() {
        printTaskStates()
        printOptions()
        goToNextView()
    }

    private fun printOptions() {
        printLn("1. Add New Task State")
        printLn("2. Edit Task State")
        printLn("3. Delete Task State")
        printLn("0. Back to Edit Project")
    }

    private suspend fun goToNextView() {
        printLn("Select an option:")
        when (cliReader.getValidInputNumberInRange(max = 3)) {
            1 -> addState()
            2 -> editState()
            3 -> deleteState()
            0 -> return
        }
        getTaskStates()
    }

    private suspend fun getTaskStates() {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading task states...")
                currentState = ViewState.Loading
            },
            onSuccess = { states ->
                tasksStates = states
                currentState = ViewState.Success(states)
                showMainMenu()
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to load task states: ${exception.message}")
                showMainMenu()
            },
            operation = {
                useCase.getTaskStatesByProjectId(projectId)
            }
        )
    }

    private fun printTaskStates() {
        if (tasksStates.isEmpty()) {
            printLn("No task states available.")
        } else {
            val headers = listOf("#", "Title", "Description")
            val data = tasksStates.mapIndexed { index, state ->
                listOf((index + 1).toString(), state.title, state.description)
            }
            cliTablePrinter(headers, data, columnsWidth)
        }
    }

    private suspend fun addState() {
        val title = taskStateInputReader.getValidTaskStateTitle()
        val description =
            taskStateInputReader.getValidTaskStateDescription() // Fixed: was calling getValidTaskStateTitle() twice

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Adding new task state...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                printLn("Task state added successfully.")
                currentState = ViewState.Success(tasksStates)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to add task state: ${exception.message}")
            },
            operation = {
                useCase.addState(title, description, projectId)
            }
        )
    }

    private suspend fun editState() {
        if (tasksStates.isEmpty()) {
            printLn("No task states available to edit.")
            return
        }

        printLn("Select a task state by number:")
        val index = cliReader.getValidInputNumberInRange(max = tasksStates.size, min = 1) - 1
        val selectedState = tasksStates[index]

        printLn("1. Edit Title")
        printLn("2. Edit Description")

        when (cliReader.getValidInputNumberInRange(max = 2, min = 1)) {
            1 -> editTaskStateTitle(selectedState)
            2 -> editTaskStateDescription(selectedState)
        }
    }

    private suspend fun editTaskStateTitle(state: TaskState) {
        val newTitle = taskStateInputReader.getValidTaskStateTitle()

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Updating title...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                printLn("Title updated successfully.")
                currentState = ViewState.Success(tasksStates)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to update title: ${exception.message}")
            },
            operation = {
                useCase.editStateTitle(state.id, newTitle)
            }
        )
    }

    private suspend fun editTaskStateDescription(state: TaskState) {
        val newDescription = taskStateInputReader.getValidTaskStateDescription()

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Updating description...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                printLn("Description updated successfully.")
                currentState = ViewState.Success(tasksStates)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to update description: ${exception.message}")
            },
            operation = {
                useCase.editStateDescription(state.id, newDescription)
            }
        )
    }

    private suspend fun deleteState() {
        if (tasksStates.isEmpty()) {
            printLn("No task states available to delete.")
            return
        }

        printLn("Select a task state by number:")
        val index = cliReader.getValidInputNumberInRange(max = tasksStates.size, min = 1) - 1
        val selectedState = tasksStates[index]

        val confirm = cliReader.getUserConfirmation()
        if (confirm) {
            viewExceptionHandler.executeWithState(
                onLoading = {
                    printLn("Deleting task state...")
                    currentState = ViewState.Loading
                },
                onSuccess = {
                    printLn("Task state deleted successfully.")
                    currentState = ViewState.Success(tasksStates)
                },
                onError = { exception ->
                    currentState = ViewState.Error(exception)
                    printLn("Failed to delete task state: ${exception.message}")
                },
                operation = {
                    useCase.deleteState(selectedState.id)
                }
            )
        } else {
            printLn("Deletion canceled.")
        }
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        val columnsWidth = listOf(5, 30, null)
    }
}