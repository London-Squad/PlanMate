package ui.statesView

import logic.entities.State
import logic.useCases.ManageStateUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class StatesView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val useCase: ManageStateUseCase
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

    private fun viewStates(): List<State> {
        val states = useCase.getStates(projectId)
        if (states.isEmpty()) {
            cliPrinter.cliPrintLn("No states available.")
        } else {
            states.forEachIndexed {index, state ->
                cliPrinter.cliPrintLn("${index+1}.${state.title}:${state.description} (ID: ${state.id})")
            }
        }
        return states
    }

    private fun addState() {
        val title = cliReader.getValidTitle()
        val desc = cliReader.getValidDescription()
        useCase.addState(State(title = title, description = desc), projectId)
        cliPrinter.cliPrintLn("State added successfully.")
    }

    private fun editState() {
         val states=viewStates()
        if(states.isEmpty())return
        val index = getValidIndex(states.size, "Enter the number of the state you want to edit (1 to ${states.size}):")
        val selectedState=states[index]
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
        val state=viewStates()
        if(state.isEmpty())return
        val index = getValidIndex(state.size, "Enter the number of the state you want to delete (1 to ${state.size}):")

        val selectedState=state[index]

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
                cliPrinter.cliPrintLn("Invalid number.")
            }
        }
    }

}