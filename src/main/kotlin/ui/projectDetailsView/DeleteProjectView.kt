package ui.projectDetailsView

import logic.useCases.ManageProjectUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val manageProjectUseCase: ManageProjectUseCase,
) : RequestHandler(cliPrinter) {
    fun deleteProject(projectId: UUID) {
        if (isDeletionCanceled()) {
            cliPrinter.cliPrintLn("Project deletion canceled")
            return
        }

        makeRequest(
            request = { manageProjectUseCase.deleteProject(projectId) },
            onSuccess = { cliPrinter.cliPrintLn("Project with id $projectId was deleted successfully.") },
            onLoadingMessage = "Deleting project..."
        )
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()
}