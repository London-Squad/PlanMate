package ui.statesView

import logic.entities.State
import logic.useCases.ManageStateUseCase
import ui.CLIPrintersAndReaders.CLIReader
import ui.CLIPrintersAndReaders.CLIPrinter

import ui.View
import java.util.*

class StatesView(
    private val printer: CLIPrinter,
    private val reader: CLIReader,
    private val useCase: ManageStateUseCase,
    private val getStates: (UUID) -> List<State>
) : View {

    override fun start() {
        printer.cliPrintLn("Please provide project ID to manage states.")
        val projectId = getUUID("Enter Project ID: ")
        showMainMenu(projectId)
    }

    private fun showMainMenu(projectId: UUID) {
        while (true) {
            printer.printHeader("States Management")
            printer.cliPrintLn("1. View States")
            printer.cliPrintLn("2. Add New State")
            printer.cliPrintLn("3. Select State")
            printer.cliPrintLn("0. Back")

            when (reader.getUserInput("Choose option: ").trim()) {
                "1" -> viewStates(projectId)
                "2" -> addState(projectId)
                "3" -> selectState(projectId)
                "4" -> return
                else -> printer.cliPrintLn("Invalid option.")
            }
        }
    }

    private fun viewStates(projectId: UUID) {
        val states = getStates(projectId)
        printer.printHeader("States in Project")
        if (states.isEmpty()) {
            printer.cliPrintLn("No states found.")
        } else {
            states.forEachIndexed { i, state ->
                printer.cliPrintLn("${i + 1}. ${state.title} (${state.id})")
            }
        }
    }

    private fun addState(projectId: UUID) {
        val title = reader.getUserInput("Enter state title: ").trim()
        val desc = reader.getUserInput("Enter state description: ").trim()
        val state = State(title = title, description = desc)
        useCase.addState(state, projectId)
        printer.cliPrintLn("State '${state.title}' added successfully.")
    }

    private fun selectState(projectId: UUID) {
        val states = getStates(projectId)
        if (states.isEmpty()) {
            printer.cliPrintLn("No states to select.")
            return
        }

        viewStates(projectId)
        val index = reader.getValidUserInput(
            { it.toIntOrNull() in 1..states.size },
            "Select state by number: ",
            "Invalid selection."
        ).toInt()

        val state = states[index - 1]
        stateMenu(state)
    }

    private fun stateMenu(state: State) {
        while (true) {
            printer.printHeader("State: ${state.title}")
            printer.cliPrintLn("1. Edit Title")
            printer.cliPrintLn("2. Edit Description")
            printer.cliPrintLn("3. Delete")
            printer.cliPrintLn("0. Back")

            when (reader.getUserInput("Choose option: ").trim()) {
                "1" -> {
                    val newTitle = reader.getUserInput("New title: ")
                    useCase.editStateTitle(state.id, newTitle)
                    printer.cliPrintLn("Title updated.")
                }
                "2" -> {
                    val newDesc = reader.getUserInput("New description: ")
                    useCase.editStateDescription(state.id, newDesc)
                    printer.cliPrintLn("Description updated.")
                }
                "3" -> {
                    if (confirmDelete(state.title)) {
                        useCase.deleteState(state.id)
                        printer.cliPrintLn("State deleted.")
                        return
                    } else {
                        printer.cliPrintLn("Deletion cancelled.")
                    }
                }
                "0" -> return
                else -> printer.cliPrintLn("Invalid option.")
            }
        }
    }

    private fun confirmDelete(title: String): Boolean {
        return when (reader.getUserInput("Delete '$title'? (y/n): ").lowercase()) {
            "y" -> true
            "n" -> false
            else -> {
                printer.cliPrintLn("Invalid input.")
                confirmDelete(title)
            }
        }
    }

    private fun getUUID(prompt: String): UUID {
        return try {
            UUID.fromString(reader.getUserInput(prompt))
        } catch (e: IllegalArgumentException) {
            printer.cliPrintLn("Invalid UUID format.")
            getUUID(prompt)
        }
    }
}