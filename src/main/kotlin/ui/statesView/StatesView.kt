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
    private val useCase: ManageStateUseCase
) : View {

    override fun start() {
        printer.cliPrintLn("This view requires a projectId to start.")
    }

    fun start(projectId: UUID) {
        while (true) {
            printer.printHeader("States Management")
            printer.cliPrintLn("1. View States")
            printer.cliPrintLn("2. Add New State")
            printer.cliPrintLn("3. Edit State")
            printer.cliPrintLn("4. Delete State")
            printer.cliPrintLn("0. Back")

            when (reader.getUserInput("Choose option: ")) {
                "1" -> viewStates(projectId)
                "2" -> addState(projectId)
                "3" -> editState(projectId)
                "4" -> deleteState(projectId)
                "0" -> return
                else -> printer.cliPrintLn("Invalid option.")
            }
        }
    }

    private fun viewStates(projectId: UUID) {
        val states = useCase.getStates(projectId)
        if (states.isEmpty()) {
            printer.cliPrintLn("No states available.")
        } else {
            states.forEach {
                printer.cliPrintLn("${it.id} - ${it.title}: ${it.description}")
            }
        }
    }

    private fun addState(projectId: UUID) {
        val title = reader.getUserInput("Enter title: ")
        val desc = reader.getUserInput("Enter description: ")
        useCase.addState(State(title = title, description = desc), projectId)
        printer.cliPrintLn("State added successfully.")
    }

    private fun editState(projectId: UUID) {
        viewStates(projectId)
        val id = getUUID("Enter state ID to edit: ")

        printer.cliPrintLn("1. Edit title")
        printer.cliPrintLn("2. Edit description")

        when (reader.getUserInput("Choose: ")) {
            "1" -> {
                val newTitle = reader.getUserInput("New title: ")
                useCase.editStateTitle(id, newTitle)
                printer.cliPrintLn("Title updated.")
            }
            "2" -> {
                val newDesc = reader.getUserInput("New description: ")
                useCase.editStateDescription(id, newDesc)
                printer.cliPrintLn("Description updated.")
            }
            else -> printer.cliPrintLn("Invalid option.")
        }
    }

    private fun deleteState(projectId: UUID) {
        viewStates(projectId)
        val id = getUUID("Enter state ID to delete: ")
        val confirm = reader.getUserInput("Are you sure? (y/n): ")
        if (confirm.lowercase() == "y") {
            useCase.deleteState(id)
            printer.cliPrintLn("State deleted.")
        } else {
            printer.cliPrintLn("Deletion canceled.")
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