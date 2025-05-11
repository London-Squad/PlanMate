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
    private val baseView: BaseView

) {
    fun deleteProject(projectId: UUID) {
        if (isDeletionCanceled()) {
            cliPrinter.cliPrintLn("Project deletion canceled")
            return
        }

        baseView.tryCall {
            manageProjectUseCase.deleteProject(projectId)
            cliPrinter.cliPrintLn("Project $projectId was deleted successfully.")
        }
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()
}