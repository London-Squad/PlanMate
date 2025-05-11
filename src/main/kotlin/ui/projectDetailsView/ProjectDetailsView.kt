package ui.projectDetailsView

import logic.entities.User
import logic.useCases.GetProjectDetailsUseCase
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.TaskInputReader
import ui.logsView.LogsView
import ui.taskManagementView.TaskManagementView
import java.util.UUID

class ProjectDetailsView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val editProjectView: EditProjectView,
    private val deleteProjectView: DeleteProjectView,
    private val getProjectDetailsUseCase: GetProjectDetailsUseCase,
    private val swimlanesView: SwimlanesView,
    private val taskInputReader: TaskInputReader,
    private val taskManagementView: TaskManagementView,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val logsView: LogsView,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private var currentState: ViewState<GetProjectDetailsUseCase.ProjectDetails> = ViewState.Loading
    private lateinit var loggedInUserType: User.Type
    private lateinit var projectDetails: GetProjectDetailsUseCase.ProjectDetails

    suspend fun start(projectId: UUID, loggedInUserType: User.Type, onComplete: () -> Unit = {}) {
        this.loggedInUserType = loggedInUserType
        loadProjectDetails(projectId, onComplete)
    }

    private suspend fun loadProjectDetails(projectId: UUID, onComplete: () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading project details...")
                currentState = ViewState.Loading
            },
            onSuccess = { details ->
                projectDetails = details
                currentState = ViewState.Success(details)
                displayProjectDetails(onComplete)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to load project details: ${exception.message}")
                onComplete()
            },
            operation = {
                getProjectDetailsUseCase(projectId)
            }
        )
    }

    private suspend fun displayProjectDetails(onComplete: () -> Unit) {
        printHeader(projectDetails.project.title)
        displaySwimlanes()
        printOptions()
        goToNextView(onComplete)
    }

    private suspend fun displaySwimlanes() {
        viewExceptionHandler.executeWithState(
            onLoading = {},
            onSuccess = {
                swimlanesView.displaySwimlanes(projectDetails.tasks, projectDetails.taskStates)
            },
            onError = { exception ->
                printLn("Failed to display swimlanes: ${exception.message}")
            },
            operation = {
                swimlanesView.displaySwimlanes(projectDetails.tasks, projectDetails.taskStates)
            }
        )
    }

    private fun printHeader(title: String) {
        cliPrinter.printHeader("Project: $title")
    }

    private fun printOptions() {
        printLn("\n1. Select a task")
        printLn("2. Create new task")
        printLn("3. View project logs")
        if (loggedInUserType == User.Type.ADMIN) {
            printLn("4. Edit project")
            printLn("5. Delete project")
        }
        printLn("0. Back to projects")
    }

    private suspend fun goToNextView(onComplete: () -> Unit) {
        when (getValidUserInput()) {
            1 -> selectTask { start(projectDetails.project.id, loggedInUserType, onComplete) }
            2 -> createNewTask { start(projectDetails.project.id, loggedInUserType, onComplete) }
            3 -> viewLogs { start(projectDetails.project.id, loggedInUserType, onComplete) }
            4 -> editProject { start(projectDetails.project.id, loggedInUserType, onComplete) }
            5 -> deleteProject { onComplete() }  // After deletion, return to previous screen
            0 -> {
                printLn("\nExiting Project...")
                onComplete()
            }
        }
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            if (loggedInUserType == User.Type.ADMIN) MAX_OPTION_NUMBER_ADMIN
            else MAX_OPTION_NUMBER_MATE

        return cliReader.getValidInputNumberInRange(
            min = 0,
            max = maxOptionNumberAllowed
        )
    }

    private suspend fun selectTask(onComplete: suspend () -> Unit) {
        if (projectDetails.tasks.isEmpty()) {
            printLn("No tasks available to select.")
            onComplete()
            return
        }

        printLn("Select a task by number:")
        val input = cliReader.getValidInputNumberInRange(
            min = 1,
            max = projectDetails.tasks.size
        )

        taskManagementView.start(
            taskId = projectDetails.tasks[input - 1].id,
            projectId = projectDetails.project.id
        )
        onComplete()
    }

    private suspend fun createNewTask(onComplete:suspend () -> Unit) {
        val title = taskInputReader.getValidTaskTitle()
        val description = taskInputReader.getValidTaskDescription()

        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Creating new task...")
                currentState = ViewState.Loading
            },
            onSuccess = {
                printLn("Task created successfully.")
                onComplete()
            },
            onError = { exception ->
                printLn("Failed to create task: ${exception.message}")
                onComplete()
            },
            operation = {
                manageTaskUseCase.addNewTask(title, description, projectDetails.project.id)
            }
        )
    }

    private suspend fun viewLogs(onComplete: suspend () -> Unit) {
        logsView.printLogsByEntityId(projectDetails.project.id) {
            onComplete()
        }
    }

    private suspend fun editProject(onComplete: suspend () -> Unit) {
        editProjectView.editProject(projectDetails.project.id) {
            onComplete()
        }
    }

    private suspend fun deleteProject(onComplete: () -> Unit) {
        deleteProjectView.deleteProject(projectDetails.project.id) {
            onComplete()
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 5
        const val MAX_OPTION_NUMBER_MATE = 3
    }
}