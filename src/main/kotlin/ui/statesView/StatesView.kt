package ui.statesView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import java.util.*

class StatesView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val useCase: ManageStateUseCase,
    private val viewExceptionHandler: ViewExceptionHandler,
    private val cliTablePrinter: CLITablePrinter = CLITablePrinter(cliPrinter)
) {

    private lateinit var projectId: UUID

    fun start(passedProjectId: UUID) {
        projectId = passedProjectId
        while (true) {
            cliPrinter.printHeader("Task States Management")
            cliPrinter.cliPrintLn("1. Add New Task State")
            cliPrinter.cliPrintLn("2. Edit Task State")
            cliPrinter.cliPrintLn("3. Delete Task State")
            cliPrinter.cliPrintLn("0. Back to Edit Project")

            when (cliReader.getValidUserNumberInRange(3)) {
                "1" -> addState()
                "2" -> editState()
                "3" -> deleteState()
                "0" -> return
            }
        }
    }

    private fun getAndPrintTaskStates(): List<TaskState> {
        var taskStates: List<TaskState> = emptyList()
        viewExceptionHandler.tryCall {
            taskStates = useCase.getStates(projectId)
            if (taskStates.isEmpty()) {
                cliPrinter.cliPrintLn("No task states available.")
            } else {
                val headers = listOf("#", "Title", "Description")
                val data = taskStates.mapIndexed { index, state ->
                    listOf((index + 1).toString(), state.title, state.description)
                }
                cliTablePrinter(headers, data, listOf(5, 25, 35))
            }
        }
        return taskStates
    }

    private fun addState() {
        val title = cliReader.getValidTitle()
        val desc = cliReader.getValidDescription()
        useCase.addState(TaskState(title = title, description = desc), projectId)
        cliPrinter.cliPrintLn("Task state added successfully.")
    }

    private fun editState() {
        val taskStates = getAndPrintTaskStates()
        if (taskStates.isEmpty()) return

        val index = cliReader.getValidUserNumberInRange(taskStates.size, min = 1).toInt() - 1
        val selectedState = taskStates[index]

        cliPrinter.cliPrintLn("1. Edit Title")
        cliPrinter.cliPrintLn("2. Edit Description")

        when (cliReader.getValidUserNumberInRange(2, min = 1)) {
            "1" -> editTaskStateTitle(selectedState)
            "2" -> editTaskStateDescription(selectedState)
        }
    }

    private fun editTaskStateTitle(state: TaskState) {
        val newTitle = cliReader.getUserInput("New title: ")
        useCase.editStateTitle(state.id, newTitle)
        cliPrinter.cliPrintLn("Title updated.")
    }

    private fun editTaskStateDescription(state: TaskState) {
        val newDescription = cliReader.getUserInput("New description: ")
        useCase.editStateDescription(state.id, newDescription)
        cliPrinter.cliPrintLn("Description updated.")
    }

    private fun deleteState() {
        val taskStates = getAndPrintTaskStates()
        if (taskStates.isEmpty()) return

        val index = cliReader.getValidUserNumberInRange(taskStates.size, min = 1).toInt() - 1
        val selectedState = taskStates[index]

        val confirm = cliReader.getUserInput("Are you sure you want to delete this task state? (y/n): ")
        if (confirm.lowercase() == "y") {
            useCase.deleteState(selectedState.id)
            cliPrinter.cliPrintLn("Task state deleted.")
        } else {
            cliPrinter.cliPrintLn("Deletion canceled.")
        }
    }
}