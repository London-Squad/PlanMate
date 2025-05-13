package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class TaskDeletionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
) : RequestHandler(cliPrinter) {
    suspend fun deleteTask(taskId: UUID) {
        if (isDeletionCanceled()) {
            cliPrinter.cliPrintLn("Deletion canceled")
            return
        }

        makeRequest(
            request = { manageTaskUseCase.deleteTask(taskId) },
            onSuccess = { cliPrinter.cliPrintLn("task $taskId was deleted") },
            onLoadingMessage = "Deleting task..."
        )
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()
}