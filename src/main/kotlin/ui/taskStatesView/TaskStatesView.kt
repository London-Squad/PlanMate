package ui.taskStatesView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.CLITablePrinter
import ui.cliPrintersAndReaders.TaskStateInputReader
import java.util.*

class TaskStatesView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val taskStateInputReader: TaskStateInputReader,
    private val useCase: ManageStateUseCase,
    private val cliTablePrinter: CLITablePrinter
) : RequestHandler(cliPrinter) {

    private lateinit var projectId: UUID
    private var taskStates: List<TaskState> = emptyList()

    fun start(projectId: UUID) {
        this.projectId = projectId

        cliPrinter.printHeader("Task States Management")

        makeRequest(
            request = { fetchTaskStates() },
            onSuccess = {
                printTaskStates()
                printOptions()
                goToNextView()
            },
            onLoadingMessage = "Fetching task states..."
        )
    }

    private fun printOptions() {
        cliPrinter.cliPrintLn("1. create New Task State")
        cliPrinter.cliPrintLn("2. Edit Task State")
        cliPrinter.cliPrintLn("3. Delete Task State")
        cliPrinter.cliPrintLn("0. Back to Edit Project")
    }

    private fun goToNextView() {
        cliPrinter.cliPrintLn("Select an option:")
        when (cliReader.getValidInputNumberInRange(3)) {
            1 -> addState()
            2 -> editState()
            3 -> deleteState()
        }
        start(projectId)
    }

    private suspend fun fetchTaskStates() {
        taskStates = useCase.getTaskStatesByProjectId(projectId)
    }

    private fun printTaskStates() {
        if (taskStates.isEmpty()) {
            cliPrinter.cliPrintLn("No task states available.")
        } else {
            val headers = listOf("#", "Title", "Description")
            val data = taskStates.mapIndexed { index, state ->
                listOf((index + 1).toString(), state.title, state.description)
            }
            cliTablePrinter(headers, data, columnsWidth)
        }
    }

    private fun addState() {
        val title = taskStateInputReader.getValidTaskStateTitle()
        val desc = taskStateInputReader.getValidTaskStateDescription()
        makeRequest(
            request = { useCase.addState(title, desc, projectId) },
            onSuccess = { cliPrinter.cliPrintLn("Task state added successfully.") },
            onLoadingMessage = "Adding task state..."
        )

    }

    private fun editState() {
        if (taskStates.isEmpty()) {
            cliPrinter.cliPrintLn("No task states available to edit.")
            return
        }

        cliPrinter.cliPrintLn("select a task state by number.")
        val index = cliReader.getValidInputNumberInRange(taskStates.size, min = 1) - 1
        val selectedState = taskStates[index]

        cliPrinter.cliPrintLn("1. Edit Title")
        cliPrinter.cliPrintLn("2. Edit Description")

        when (cliReader.getValidInputNumberInRange(2, min = 1)) {
            1 -> editTaskStateTitle(selectedState)
            2 -> editTaskStateDescription(selectedState)
        }
    }

    private fun editTaskStateTitle(state: TaskState) {
        val newTitle = taskStateInputReader.getValidTaskStateTitle()
        makeRequest(
            request = { useCase.editStateTitle(state.id, newTitle) },
            onSuccess = { cliPrinter.cliPrintLn("Title updated successfully.") },
            onLoadingMessage = "Updating task state title..."
        )
    }

    private fun editTaskStateDescription(state: TaskState) {
        val newDescription = taskStateInputReader.getValidTaskStateDescription()
        makeRequest(
            request = { useCase.editStateDescription(state.id, newDescription) },
            onSuccess = { cliPrinter.cliPrintLn("Description updated successfully.") },
            onLoadingMessage = "Updating task state description..."
        )
    }

    private fun deleteState() {
        if (taskStates.isEmpty()) {
            cliPrinter.cliPrintLn("No task states available to edit.")
            return
        }
        cliPrinter.cliPrintLn("select a task state by number.")
        val index = cliReader.getValidInputNumberInRange(taskStates.size, min = 1) - 1
        val selectedState = taskStates[index]

        if (!cliReader.getUserConfirmation()) {
            cliPrinter.cliPrintLn("Deletion canceled.")
            return
        }

        makeRequest(
            request = { useCase.deleteState(selectedState.id) },
            onSuccess = { cliPrinter.cliPrintLn("Task state deleted successfully.") },
            onLoadingMessage = "Deleting task state..."
        )
    }

    private companion object {
        val columnsWidth = listOf(5, 30, null)
    }
}