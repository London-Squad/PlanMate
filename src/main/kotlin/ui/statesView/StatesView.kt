package ui.statesView

import logic.entities.State
import logic.useCases.ManageStateUseCase
import ui.CLIPrintersAndReaders.CLIPrinter
import ui.CLIPrintersAndReaders.CLIReader
import ui.View
import java.util.*

class StatesView(
    private val printer: CLIPrinter,
    private val reader: CLIReader,
    private val useCase: ManageStateUseCase,
    private val projectId: UUID,
    private val getStates: () -> List<State>
) : View {

    override fun start() {
        while (true) {
            printMenu()
            when (prompt("Choose option: ")) {
                "1" -> viewStates()
                "2" -> addState()
                "3" -> selectState()
                "0"-> return
                else -> printer.cliPrintLn("Invalid option.")
            }
        }
    }

    private fun printMenu() {
        printer.printHeader("States Management")
        printer.cliPrintLn("1. View States")
        printer.cliPrintLn("2. Add New State")
        printer.cliPrintLn("3. Select State")
        printer.cliPrintLn("0. Back")
    }

    private fun prompt(message: String): String {
        return reader.getUserInput(message).trim()
    }

    private fun viewStates() {
        val states = getStates()
        printer.printHeader("States in Project")
        if (states.isEmpty()) {
            printer.cliPrintLn("There are no states in this project yet.")
        } else {
            displayStateList(states)
        }
    }

    private fun displayStateList(states: List<State>) {
        states.forEachIndexed { index, state ->
            printer.cliPrintLn("${index + 1}. ${state.title} (ID: ${state.id})")
        }
    }

    private fun addState() {
        val title = prompt("Enter state title: ")
        val desc = prompt("Enter state description: ")
        val state = State(title = title, description = desc)
        useCase.addState(state, projectId)
        printer.cliPrintLn("State '${state.title}' added successfully.")
    }

    private fun selectState() {
        val states = getStates()
        if (states.isEmpty()) {
            printer.cliPrintLn("No states available to select.")
            return
        }

        viewStates()
        val index = reader.getValidUserInput(
            { it.toIntOrNull() in 0..states.size },
            "Select a state by number (0 to go back): ",
            "Invalid choice."
        ).toInt()

        if (index == 0) return
        val selectedState = states[index - 1]
        selectedStateMenu(selectedState)
    }

    private fun selectedStateMenu(state: State) {
        while (true) {
            printer.printHeader("State: ${state.title}")
            printer.cliPrintLn("1. Edit Title")
            printer.cliPrintLn("2. Edit Description")
            printer.cliPrintLn("3. Delete")
            printer.cliPrintLn("0. Back")

            when (prompt("Choose option: ")) {
                "1" -> {
                    val newTitle = prompt("Enter new title: ")
                    useCase.editStateTitle(state.id, newTitle)
                    printer.cliPrintLn("Title updated to '$newTitle'.")
                }
                "2" -> {
                    val newDesc = prompt("Enter new description: ")
                    useCase.editStateDescription(state.id, newDesc)
                    printer.cliPrintLn("Description updated.")
                }
                "3" -> {
                    useCase.deleteState(state.id)
                    printer.cliPrintLn("State '${state.title}' deleted.")
                    break
                }
                "0" -> return
                else -> printer.cliPrintLn("Invalid option.")
            }
        }
    }
}