package ui.projectDetailsView

import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val viewExceptionHandler: ViewExceptionHandler

) {
    fun deleteProject(projectId: UUID) {
        if (isDeletionCanceled()) {
            cliPrinter.cliPrintLn("Project deletion canceled")
            return
        }

        viewExceptionHandler.tryCall {
            manageProjectUseCase.deleteProject(projectId)
            cliPrinter.cliPrintLn("Project $projectId was deleted successfully.")
        }
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()
}