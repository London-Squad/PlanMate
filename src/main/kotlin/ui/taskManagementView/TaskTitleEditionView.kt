package ui.taskManagementView

import logic.entities.Task
import logic.exceptions.NotFoundException
import logic.useCases.ManageTaskUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskTitleEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase
) {

    fun editTitle(task: Task) {
        val newTitle = getValidTitle()

        try {
            manageTaskUseCase.editTaskTitle(task.id, newTitle)
        } catch (e: NotFoundException) {
            cliPrinter.cliPrintLn(e.message ?: "task not found")
        }
    }

    private fun getValidTitle(): String {
        val userInput = cliReader.getUserInput("New Title ($MAX_TITLE_LENGTH character or less): ").trim()
        if (userInput.isNotBlank() && userInput.length <= MAX_TITLE_LENGTH) return userInput
        else {
            cliPrinter.cliPrintLn("Invalid Title")
            return getValidTitle()
        }
    }

    private companion object {
        const val MAX_TITLE_LENGTH = 30
    }
}