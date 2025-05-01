package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskDescriptionEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase
) {

    fun editDescription(task: Task) {
        val newDescription = getValidDescription()
        manageTaskUseCase.editTaskDescription(task.id, newDescription)
    }

    private fun getValidDescription(): String {
        val userInput = cliReader.getUserInput("New Description ($MAX_DESCRIPTION_LENGTH character or less): ").trim()
        if (userInput.length <= MAX_DESCRIPTION_LENGTH) return userInput
        else {
            cliPrinter.cliPrintLn("Invalid description")
            return getValidDescription()
        }
    }

    private companion object {
        const val MAX_DESCRIPTION_LENGTH = 150
    }
}