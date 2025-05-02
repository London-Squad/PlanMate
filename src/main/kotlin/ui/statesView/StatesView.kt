package ui.statesView

import logic.entities.State
import logic.useCases.ManageStateUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class StatesView(
    private val printer: CLIPrinter,
    private val reader: CLIReader,
    private val useCase: ManageStateUseCase
) {

    private lateinit var projectId: UUID

    fun start(passedProjectId: UUID) {
        projectId = passedProjectId
        while (true) {
            printer.printHeader("States Management")
            printer.cliPrintLn("1. View States")
            printer.cliPrintLn("2. Add New State")
            printer.cliPrintLn("3. Edit State")
            printer.cliPrintLn("4. Delete State")
            printer.cliPrintLn("0. Back to Edit Project")

            when (reader.getUserInput("Choose option: ")) {
                "1" -> viewStates()
                "2" -> addState()
                "3" -> editState()
                "4" -> deleteState()
                "0" -> return
                else -> printer.cliPrintLn("Invalid option.")
            }
        }
    }

    private fun viewStates() {
        val states = useCase.getStates(projectId)
        if (states.isEmpty()) {
            printer.cliPrintLn("No states available.")
        } else {
            states.forEach {
                printer.cliPrintLn("${it.id} - ${it.title}: ${it.description}")
            }
        }
    }

    private fun addState() {
        val title = reader.getUserInput("Enter title: ")
        val desc = reader.getUserInput("Enter description: ")
        useCase.addState(State(title = title, description = desc), projectId)
        printer.cliPrintLn("State added successfully.")
    }

    private fun editState() {
        viewStates()
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

    private fun deleteState() {
        viewStates()
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