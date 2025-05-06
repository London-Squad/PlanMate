package ui.tasksStatesView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import java.util.*

class TasksStatesView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val useCase: ManageStateUseCase,
    private val viewExceptionHandler: ViewExceptionHandler,
    private val cliTablePrinter: CLITablePrinter = CLITablePrinter(cliPrinter)
) {

    private lateinit var projectId: UUID
    private var tasksStates: List<TaskState> = emptyList()

    fun start(projectId: UUID) {
        this.projectId = projectId

        cliPrinter.printHeader("Task States Management")
        getTaskStates()
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

    private fun goToNextView() {
        printLn("Select an option:")
        when (cliReader.getValidUserNumberInRange(3)) {
            1 -> addState()
            2 -> editState()
            3 -> deleteState()
            0 -> return
        }
        start(projectId)
    }

    private fun getTaskStates() {
        viewExceptionHandler.tryCall {
            tasksStates = useCase.getStates(projectId)
        }
    }

    private fun printTaskStates() {
        viewExceptionHandler.tryCall {

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
    }

    private fun addState() {
        val title = cliReader.getValidTitle()
        val desc = cliReader.getValidDescription()
        useCase.addState(TaskState(title = title, description = desc), projectId)
        printLn("Task state added successfully.")
    }

    private fun editState() {
        if (tasksStates.isEmpty()) {
            printLn("No task states available to edit.")
            return
        }

        printLn("select a task state by number.")
        val index = cliReader.getValidUserNumberInRange(tasksStates.size, min = 1) - 1
        val selectedState = tasksStates[index]

        printLn("1. Edit Title")
        printLn("2. Edit Description")

        when (cliReader.getValidUserNumberInRange(2, min = 1)) {
            1 -> editTaskStateTitle(selectedState)
            2 -> editTaskStateDescription(selectedState)
        }
    }

    private fun editTaskStateTitle(state: TaskState) {
        val newTitle = cliReader.getUserInput("New title: ")
        useCase.editStateTitle(state.id, newTitle)
        printLn("Title updated.")
    }

    private fun editTaskStateDescription(state: TaskState) {
        val newDescription = cliReader.getUserInput("New description: ")
        useCase.editStateDescription(state.id, newDescription)
        printLn("Description updated.")
    }

    private fun deleteState() {
        if (tasksStates.isEmpty()) {
            printLn("No task states available to edit.")
            return
        }
        printLn("select a task state by number.")
        val index = cliReader.getValidUserNumberInRange(tasksStates.size, min = 1) - 1
        val selectedState = tasksStates[index]

        val confirm = cliReader.getUserInput("Are you sure you want to delete this task state? (y/n): ")
        if (confirm.lowercase() == "y") {
            useCase.deleteState(selectedState.id)
            printLn("Task state deleted.")
        } else {
            printLn("Deletion canceled.")
        }
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        val columnsWidth = listOf(5, 30, null)
    }
}