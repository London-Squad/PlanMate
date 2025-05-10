package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class TaskDeletionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    fun deleteTask(taskId: UUID) {
        if (isDeletionCanceled()) {
            cliPrinter.cliPrintLn("deletion canceled")
            return
        }

        viewExceptionHandler.tryCall {
            manageTaskUseCase.deleteTask(taskId).also {
                cliPrinter.cliPrintLn("task $taskId was deleted")
            }
        }
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()
}