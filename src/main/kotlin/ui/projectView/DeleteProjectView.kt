package ui.projectView

import logic.entities.Project
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter

class DeleteProjectView(
    private val cliPrinter: CLIPrinter,
    private val projectUseCases: ProjectUseCases
) {

    fun deleteProject(project: Project) {
        projectUseCases.deleteProject(project.id)
        cliPrinter.cliPrintLn("Project deleted.")
    }
}