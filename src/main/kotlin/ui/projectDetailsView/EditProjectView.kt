package ui.projectDetailsView

import logic.entities.Project
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.ProjectInputReader
import ui.taskStatesView.TaskStatesView
import java.util.UUID

class EditProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectInputReader: ProjectInputReader,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val taskStatesView: TaskStatesView,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private var currentState: ViewState<Project> = ViewState.Loading
    private lateinit var currentProject: Project

   suspend fun editProject(projectId: UUID, onComplete: suspend () -> Unit = {}) {
        loadProject(projectId) {
            showEditOptions(onComplete)
        }
    }

    private suspend fun loadProject(projectId: UUID, onComplete: suspend () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading project details...")
                currentState = ViewState.Loading
            },
            onSuccess = { project ->
                currentProject = project
                currentState = ViewState.Success(project)
                onComplete()
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to load project: ${exception.message}")
            },
            operation = {
                manageProjectUseCase.getProjectById(projectId)
            }
        )
    }

    private suspend fun showEditOptions(onComplete: suspend () -> Unit) {
        printHeader("Edit Project: ${currentProject.title}")
        printLn("1. Edit title")
        printLn("2. Edit description")
        printLn("3. States management")
        printLn("0. Back to project")

        when (cliReader.getValidInputNumberInRange(
            min = 0,
            max = MAX_OPTION_NUMBER
        )) {
            1 -> editProjectTitle { editProject(currentProject.id, onComplete) }
            2 -> editProjectDescription { editProject(currentProject.id, onComplete) }
            3 -> statesManagement { editProject(currentProject.id, onComplete) }
            0 -> onComplete()
        }
    }

    private suspend fun editProjectTitle(onComplete: suspend () -> Unit) {
        val newTitle = projectInputReader.getValidProjectTitle()

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Updating project title...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                currentProject = currentProject.copy(title = newTitle)
                currentState = ViewState.Success(currentProject)
                printLn("Project title updated successfully.")
                onComplete()
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to update project title: ${exception.message}")
                onComplete()
            },
            operation = {
                manageProjectUseCase.editProjectTitle(currentProject.id, newTitle)
            }
        )
    }

    private suspend fun editProjectDescription(onComplete: suspend () -> Unit) {
        val newDescription = projectInputReader.getValidProjectDescription()

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Updating project description...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                currentProject = currentProject.copy(description = newDescription)
                currentState = ViewState.Success(currentProject)
                printLn("Project description updated successfully.")
                onComplete()
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to update project description: ${exception.message}")
                onComplete()
            },
            operation = {
                manageProjectUseCase.editProjectDescription(currentProject.id, newDescription)
            }
        )
    }

    private suspend fun statesManagement(onComplete: suspend () -> Unit) {
        taskStatesView.start(currentProject.id)
        onComplete()
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private fun printHeader(header: String) {
        cliPrinter.printHeader(header)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 3
    }
}