package ui.projectDetailsView

import logic.useCases.ManageProjectUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val manageProjectUseCase: ManageProjectUseCase,
) : BaseView(cliPrinter) {
    fun deleteProject(projectId: UUID) {
        if (isDeletionCanceled()) {
            cliPrinter.cliPrintLn("Project deletion canceled")
            return
        }

        tryCall({
            manageProjectUseCase.deleteProject(projectId)
            cliPrinter.cliPrintLn("Project with id $projectId was deleted successfully.")
        })
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()
}