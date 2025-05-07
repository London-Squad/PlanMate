package ui.projectDetailsView

import logic.entities.Project
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val viewExceptionHandler: ViewExceptionHandler

) {
    fun deleteProject(project: Project) {
        if (isDeletionCanceled()) {
            cliPrinter.cliPrintLn("Project deletion canceled")
            return
        }

        viewExceptionHandler.tryCall {
            manageProjectUseCase.deleteProject(project.id)
            cliPrinter.cliPrintLn("Project ${project.id} was deleted successfully.")
        }
    }

    private fun isDeletionCanceled(): Boolean = !cliReader.getUserConfirmation()
}