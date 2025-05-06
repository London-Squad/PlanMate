package ui.statesView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class StatesView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val useCase: ManageStateUseCase,
    private val viewExceptionHandler: ViewExceptionHandler

) {

    private lateinit var projectId: UUID

    fun start(passedProjectId: UUID) {
        projectId = passedProjectId
        while (true) {
            cliPrinter.printHeader("States Management")
            cliPrinter.cliPrintLn("1. View States")
            cliPrinter.cliPrintLn("2. Add New State")
            cliPrinter.cliPrintLn("3. Edit State")
            cliPrinter.cliPrintLn("4. Delete State")
            cliPrinter.cliPrintLn("0. Back to Edit Project")

            when (cliReader.getUserInput("Choose option: ")) {
                "1" -> viewStates()
                "2" -> addState()
                "3" -> editState()
                "4" -> deleteState()
                "0" -> return
                else -> cliPrinter.cliPrintLn("Invalid option.")
            }
        }
    }

    private fun viewStates():List<TaskState> {
        var states: List<TaskState> = emptyList()
        viewExceptionHandler.tryCall {
            states = useCase.getStates(projectId)
            if (states.isEmpty()) {
                cliPrinter.cliPrintLn("No states available.")
            } else {
                states.forEachIndexed { index, state ->
                    cliPrinter.cliPrintLn("${index + 1} - ${state.title}: ${state.description}")
                }
            }
        }
        return states
    }

    private fun addState() {
        val title = cliReader.getValidTitle()
        val desc = cliReader.getValidDescription()
        useCase.addState(TaskState(title = title, description = desc), projectId)
        cliPrinter.cliPrintLn("State added successfully.")
    }

    private fun editState() {
         val states =viewStates()
      if (states.isEmpty()) return
        val  index = getValidIndex(states.size ,"Enter the number (1 to ${states.size}) of the state to edit: ")
        val selectedState = states[index]

        cliPrinter.cliPrintLn("1. Edit title")
        cliPrinter.cliPrintLn("2. Edit description")

        when (cliReader.getUserInput("Choose: ")) {
            "1" -> {
                val newTitle = cliReader.getUserInput("New title: ")
                useCase.editStateTitle(selectedState.id, newTitle)
                cliPrinter.cliPrintLn("Title updated.")
            }

            "2" -> {
                val newDesc = cliReader.getUserInput("New description: ")
                useCase.editStateDescription(selectedState.id, newDesc)
                cliPrinter.cliPrintLn("Description updated.")
            }

            else -> cliPrinter.cliPrintLn("Invalid option.")
        }
    }

    private fun deleteState() {
         val states= viewStates()
        if (states.isEmpty())return
        val index = getValidIndex(states.size, "Enter the number (1 to ${states.size}) of the state to delete: ")
        val selectedState = states[index]
        val confirm = cliReader.getUserInput("Are you sure? (y/n): ")
        if (confirm.lowercase() == "y") {
            useCase.deleteState(selectedState.id)
            cliPrinter.cliPrintLn("State deleted.")
        } else {
            cliPrinter.cliPrintLn("Deletion canceled.")
        }
    }
    private fun getValidIndex(max: Int, prompt: String): Int {
        while (true) {
            val input = cliReader.getUserInput(prompt)
            val index = input.toIntOrNull()
            if (index != null && index in 1..max) {
                return index - 1
            } else {
                cliPrinter.cliPrintLn("Please enter a number between 1 and $max.")
            }
        }
    }

    private fun getUUID(prompt: String): UUID =
        UUID.fromString(cliReader.getUserInput(prompt))
}
