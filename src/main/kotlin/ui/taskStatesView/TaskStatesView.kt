package ui.taskStatesView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.TaskStateInputReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import java.util.*

class TaskStatesView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val taskStateInputReader: TaskStateInputReader,
    private val useCase: ManageStateUseCase,
    private val cliTablePrinter: CLITablePrinter
) : BaseView(cliPrinter) {

    private lateinit var projectId: UUID
    private var tasksStates: List<TaskState> = emptyList()

    fun start(projectId: UUID) {
        this.projectId = projectId

        tryCall({ fetchTaskStates() }).also { success -> if (!success) return }

        cliPrinter.printHeader("Task States Management")
        printTaskStates()
        printOptions()
        goToNextView()
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
            0 -> return
        }
        start(projectId)
    }

    private fun fetchTaskStates() {
            tasksStates = useCase.getTaskStatesByProjectId(projectId)
    }

    private fun printTaskStates() {
        if (tasksStates.isEmpty()) {
            cliPrinter.cliPrintLn("No task states available.")
        } else {
            val headers = listOf("#", "Title", "Description")
            val data = tasksStates.mapIndexed { index, state ->
                listOf((index + 1).toString(), state.title, state.description)
            }
            cliTablePrinter(headers, data, columnsWidth)
        }
    }

    private fun addState() {
        val title = taskStateInputReader.getValidTaskStateTitle()
        val desc = taskStateInputReader.getValidTaskStateDescription()
        useCase.addState(title, desc, projectId)
        cliPrinter.cliPrintLn("Task state added successfully.")
    }

    private fun editState() {
        if (tasksStates.isEmpty()) {
            cliPrinter.cliPrintLn("No task states available to edit.")
            return
        }

        cliPrinter.cliPrintLn("select a task state by number.")
        val index = cliReader.getValidInputNumberInRange(tasksStates.size, min = 1) - 1
        val selectedState = tasksStates[index]

        cliPrinter.cliPrintLn("1. Edit Title")
        cliPrinter.cliPrintLn("2. Edit Description")

        when (cliReader.getValidInputNumberInRange(2, min = 1)) {
            1 -> editTaskStateTitle(selectedState)
            2 -> editTaskStateDescription(selectedState)
        }
    }

    private fun editTaskStateTitle(state: TaskState) {
        val newTitle = taskStateInputReader.getValidTaskStateTitle()
        useCase.editStateTitle(state.id, newTitle)
        cliPrinter.cliPrintLn("Title updated.")
    }

    private fun editTaskStateDescription(state: TaskState) {
        val newDescription = taskStateInputReader.getValidTaskStateDescription()
        useCase.editStateDescription(state.id, newDescription)
        cliPrinter.cliPrintLn("Description updated.")
    }

    private fun deleteState() {
        if (tasksStates.isEmpty()) {
            cliPrinter.cliPrintLn("No task states available to edit.")
            return
        }
        cliPrinter.cliPrintLn("select a task state by number.")
        val index = cliReader.getValidInputNumberInRange(tasksStates.size, min = 1) - 1
        val selectedState = tasksStates[index]

        val confirm = cliReader.getUserConfirmation()
        if (confirm) {
            useCase.deleteState(selectedState.id)
            cliPrinter.cliPrintLn("Task state deleted.")
        } else {
            cliPrinter.cliPrintLn("Deletion canceled.")
        }
    }

    private companion object {
        val columnsWidth = listOf(5, 30, null)
    }
}