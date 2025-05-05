package ui.projectView

import logic.entities.Project
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases
) {

    fun deleteProject(project: Project) {
        if (isCancelDelete()) {
            cliPrinter.cliPrintLn("Project deletion canceled")
            return
        }

        projectUseCases.deleteProject(project.id)
        cliPrinter.cliPrintLn("Project ${project.id} was deleted successfully.")
    }

    private fun isCancelDelete(): Boolean {
        return when (cliReader.getUserInput("Are you sure to delete the project? (y/n): ")) {
            "y" -> false
            "n" -> true
            else -> {
                cliPrinter.cliPrintLn("Invalid input")
                isCancelDelete()
            }
        }
    }
}