package ui.projectDetailsView

import logic.entities.Project
import logic.useCases.ProjectUseCases
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val viewExceptionHandler: ViewExceptionHandler

) {
    fun deleteProject(project: Project) {
        if (isCancelDelete()) {
            cliPrinter.cliPrintLn("Project deletion canceled")
            return
        }
        viewExceptionHandler.tryCall {
            projectUseCases.deleteProject(project.id)
        }
        cliPrinter.cliPrintLn("Project ${project.id} was deleted successfully.")
    }

    private fun isCancelDelete(): Boolean =
        cliReader.getUserConfirmation() == "n"
}