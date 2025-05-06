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

    private fun viewStates() {
        var tasksStates: List<TaskState>
        viewExceptionHandler.tryCall {
            tasksStates = useCase.getStates(projectId)
            if (tasksStates.isEmpty()) {
                cliPrinter.cliPrintLn("No states available.")
            } else {
                tasksStates.forEach {
                    cliPrinter.cliPrintLn("${it.id} - ${it.title}: ${it.description}")
                }
            }
        }
    }

    private fun addState() {
        val title = cliReader.getValidTitle()
        val desc = cliReader.getValidDescription()
        useCase.addState(TaskState(title = title, description = desc), projectId)
        cliPrinter.cliPrintLn("State added successfully.")
    }

    private fun editState() {
        viewStates()
        val id = getUUID("Enter state ID to edit: ")

        cliPrinter.cliPrintLn("1. Edit title")
        cliPrinter.cliPrintLn("2. Edit description")

        when (cliReader.getUserInput("Choose: ")) {
            "1" -> {
                val newTitle = cliReader.getUserInput("New title: ")
                useCase.editStateTitle(id, newTitle)
                cliPrinter.cliPrintLn("Title updated.")
            }

            "2" -> {
                val newDesc = cliReader.getUserInput("New description: ")
                useCase.editStateDescription(id, newDesc)
                cliPrinter.cliPrintLn("Description updated.")
            }

            else -> cliPrinter.cliPrintLn("Invalid option.")
        }
    }

    private fun deleteState() {
        viewStates()
        val id = getUUID("Enter state ID to delete: ")
        val confirm = cliReader.getUserInput("Are you sure? (y/n): ")
        if (confirm.lowercase() == "y") {
            useCase.deleteState(id)
            cliPrinter.cliPrintLn("State deleted.")
        } else {
            cliPrinter.cliPrintLn("Deletion canceled.")
        }
    }

    private fun getUUID(prompt: String): UUID =
        UUID.fromString(cliReader.getUserInput(prompt))
}