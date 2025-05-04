package ui.taskManagementView

import logic.entities.Task
import logic.exceptions.NotFoundException
import logic.useCases.ManageTaskUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskDeletionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase
) {
    fun deleteTask(task: Task) {
        if (isCancelDelete()) {
            cliPrinter.cliPrintLn("deletion canceled")
            return
        }
        try {
            manageTaskUseCase.deleteTask(task.id).also {
                cliPrinter.cliPrintLn("task ${task.title} was deleted")
            }
        } catch (e: NotFoundException) {
            cliPrinter.cliPrintLn(e.message ?: "task not found")
        }
    }

    private fun isCancelDelete(): Boolean {

        return when (cliReader.getUserInput("Are you sure to delete the task? (y/n): ")) {
            "y" -> false
            "n" -> true
            else -> {
                cliPrinter.cliPrintLn("Invalid input")
                isCancelDelete()
            }
        }
    }
}