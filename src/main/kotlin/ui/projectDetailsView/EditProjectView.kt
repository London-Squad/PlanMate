package ui.projectDetailsView

import logic.entities.Project
import logic.useCases.ManageProjectUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.ProjectInputReader
import ui.taskStatesView.TaskStatesView
import java.util.*

class EditProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectInputReader: ProjectInputReader,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val taskStatesView: TaskStatesView,
) : BaseView(cliPrinter) {

    private lateinit var currentProject: Project

    fun editProject(projectId: UUID) {
        makeRequest(
            request = { fetchProject(projectId) },
            onSuccess = {
                printOptions()
                selectOption()
            }
        )
    }

    private fun selectOption() {
        when (cliReader.getValidInputNumberInRange(MAX_OPTION_NUMBER)) {
            1 -> editProjectTitle()
            2 -> editProjectDescription()
            3 -> statesManagement()
        }
    }

    private fun printOptions() {
        cliPrinter.printHeader("Edit Project: ${currentProject.title}")
        cliPrinter.cliPrintLn("1. Edit title")
        cliPrinter.cliPrintLn("2. Edit description")
        cliPrinter.cliPrintLn("3. States management")
        cliPrinter.cliPrintLn("0. Back to project")
    }

    private suspend fun fetchProject(projectId: UUID) {
        currentProject = manageProjectUseCase.getProjectById(projectId)
    }

    private fun editProjectTitle() {
        val newTitle = projectInputReader.getValidProjectTitle()
        makeRequest({
            manageProjectUseCase.editProjectTitle(currentProject.id, newTitle)
            cliPrinter.cliPrintLn("Project title updated successfully.")
        })
    }

    private fun editProjectDescription() {
        val newDescription = projectInputReader.getValidProjectDescription()
        makeRequest({
            manageProjectUseCase.editProjectDescription(currentProject.id, newDescription)
            cliPrinter.cliPrintLn("Project description updated successfully.")
        })
    }

    private fun statesManagement() {
        taskStatesView.start(currentProject.id)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 3
    }
}