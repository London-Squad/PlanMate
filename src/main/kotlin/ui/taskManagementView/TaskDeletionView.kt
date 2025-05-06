package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskDeletionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    fun deleteTask(task: Task) {
        if (isCancelDelete()) {
            cliPrinter.cliPrintLn("deletion canceled")
            return
        }

        viewExceptionHandler.tryCall {
            manageTaskUseCase.deleteTask(task.id).also {
                cliPrinter.cliPrintLn("task ${task.title} was deleted")
            }
        }
    }

    private fun isCancelDelete(): Boolean =
        cliReader.getUserConfirmation() == "n"
}