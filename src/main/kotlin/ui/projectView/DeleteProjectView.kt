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
            cliPrinter.cliPrintLn("deletion canceled")
            return
        }

        projectUseCases.deleteProject(project.id)
        cliPrinter.cliPrintLn("Project deleted.")
    }

    private fun isCancelDelete(): Boolean {
        return when (cliReader.getUserInput("Are you sure to delete the task? (y/n): ")) {
            "y" -> false
            "n" -> true
            else -> {
                cliPrinter.cliPrintLn("Invalid input")
                isCancelDelete()
            }
        }
    }
}