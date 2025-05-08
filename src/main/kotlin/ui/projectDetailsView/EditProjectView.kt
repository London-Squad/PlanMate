package ui.projectDetailsView

import logic.entities.Project
import logic.useCases.ProjectUseCases
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.ProjectInputReader
import ui.tasksStatesView.TasksStatesView

class EditProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectInputReader: ProjectInputReader,
    private val projectUseCases: ProjectUseCases,
    private val tasksStatesView: TasksStatesView,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    private lateinit var currentProject: Project

    fun editProject(project: Project) {
        currentProject = project
        cliPrinter.printHeader("Edit Project: ${currentProject.title}")
        cliPrinter.cliPrintLn("1. Edit title")
        cliPrinter.cliPrintLn("2. Edit description")
        cliPrinter.cliPrintLn("3. States management")
        cliPrinter.cliPrintLn("0. Back to project")

        when (cliReader.getValidInputNumberInRange(MAX_OPTION_NUMBER)) {
            1 -> editProjectTitle()
            2 -> editProjectDescription()
            3 -> statesManagement()
            0 -> return
        }
    }

    private fun editProjectTitle() {
        val newTitle = projectInputReader.getValidProjectTitle()
        viewExceptionHandler.tryCall {
            projectUseCases.editProjectTitle(currentProject.id, newTitle)
        }
        currentProject = currentProject.copy(title = newTitle)
        cliPrinter.cliPrintLn("Project title updated.")
        cliPrinter.printHeader("Edit Project: ${currentProject.title}") // Show updated header
    }

    private fun editProjectDescription() {
        val newDescription = projectInputReader.getValidProjectDescription()
        viewExceptionHandler.tryCall {
            projectUseCases.editProjectDescription(currentProject.id, newDescription)
        }
        currentProject = currentProject.copy(description = newDescription)
        cliPrinter.cliPrintLn("Project description updated.")
    }

    private fun statesManagement() {
        tasksStatesView.start(currentProject.id)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 3
    }
}