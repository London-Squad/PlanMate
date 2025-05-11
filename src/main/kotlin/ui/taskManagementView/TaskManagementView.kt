package ui.taskManagementView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import logic.entities.Task
import logic.useCases.ManageStateUseCase
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.logsView.LogsView
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class TaskManagementView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val taskTitleEditionView: TaskTitleEditionView,
    private val taskDescriptionEditionView: TaskDescriptionEditionView,
    private val taskStateEditionView: TaskStateEditionView,
    private val taskDeletionView: TaskDeletionView,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val manageStateUseCase: ManageStateUseCase,
    private val logsView: LogsView,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private var currentViewState: ViewState<Pair<Task, String>> = ViewState.Loading
    private lateinit var taskProjectId: UUID

    suspend fun start(taskId: UUID, projectId: UUID) {
        taskProjectId = projectId
        loadTaskDetails(taskId)
    }

    private suspend fun loadTaskDetails(taskId: UUID) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading task details...")
                currentViewState = ViewState.Loading
            },
            onSuccess = { taskWithState ->
                currentViewState = ViewState.Success(taskWithState)
                val (task, state) = taskWithState
                printTask(task, state)
                printOptions()
                selectNextUI(task)
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                printLn("Failed to load task details: ${exception.message}")
            },
            operation = {
                val task = manageTaskUseCase.getTaskByID(taskId)
                val state = manageStateUseCase.getTaskStatesById(task.taskStateId).title
                Pair(task, state)
            }
        )
    }

    private fun printTask(task: Task, taskState: String) {
        cliPrinter.printHeader("Task Title: ${task.title}")
        printLn("Details:")
        printLn("  - Description: ${task.description}")
        printLn("  - State: $taskState")
        printLn("")
    }

    private fun printOptions() {
        printLn("Options:")
        printLn("1. Edit Title")
        printLn("2. Edit description")
        printLn("3. Edit state")
        printLn("4. Delete task")
        printLn("5. View task logs")
        printLn("0. Back")
    }

    private suspend fun selectNextUI(task: Task) {
        when (cliReader.getValidInputNumberInRange(min = 0, max = MAX_OPTION_NUMBER)) {
            1 -> navigateToEditTitle(task)
            2 -> navigateToEditDescription(task)
            3 -> navigateToEditState(task)
            4 -> navigateToDeleteTask(task)
            5 -> navigateToViewLogs(task)
            0 -> return
        }
    }

    private suspend fun navigateToEditTitle(task: Task) {
        taskTitleEditionView.editTitle(task.id) {
            start(task.id, taskProjectId)
        }
    }

    private suspend fun navigateToEditDescription(task: Task) {
        taskDescriptionEditionView.editDescription(task.id) {
            start(task.id, taskProjectId)
        }
    }

    private suspend fun navigateToEditState(task: Task) {
        taskStateEditionView.editState(task.id, taskProjectId) {
            start(task.id, taskProjectId)
        }
    }

    private suspend fun navigateToDeleteTask(task: Task) {
        taskDeletionView.deleteTask(task.id)
    }

    private suspend fun navigateToViewLogs(task: Task) {
        logsView.printLogsByEntityId(task.id) {
            start(task.id, taskProjectId)
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 5
    }
}