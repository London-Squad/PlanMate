package ui.projectDetailsView

import logic.entities.User
import logic.useCases.GetProjectDetailsUseCase
import logic.useCases.ManageTaskUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.TaskInputReader
import ui.logsView.LogsView
import ui.taskManagementView.TaskManagementView
import java.util.*

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
    private val logsView: LogsView
) : RequestHandler(cliPrinter) {

    private lateinit var loggedInUserType: User.Type
    private lateinit var projectDetails: GetProjectDetailsUseCase.ProjectDetails

    suspend fun start(projectId: UUID, loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType

        makeRequest(
            request = { projectDetails = getProjectDetailsUseCase(projectId) },
            onSuccess = {
                printProject()
                printOptions()
                goToNextView()
            },
            onLoadingMessage = "Fetching project details..."
        )
    }

    private fun printProject() {
        cliPrinter.printHeader("Project: ${projectDetails.project.title}")
        swimlanesView.displaySwimlanes(projectDetails.tasks, projectDetails.taskStates)
    }

    private fun printOptions() {
        cliPrinter.cliPrintLn("\n1. select a task")
        cliPrinter.cliPrintLn("2. create new tasks")
        cliPrinter.cliPrintLn("3. View project logs")
        if (loggedInUserType == User.Type.ADMIN) {
            cliPrinter.cliPrintLn("4. Edit project")
            cliPrinter.cliPrintLn("5. Delete project")
        }
        cliPrinter.cliPrintLn("0. Back to projects")
    }

    private suspend fun goToNextView() {
        when (getValidUserInput()) {
            1 -> selectTask()
            2 -> createNewTask()
            3 -> logsView.printLogsByEntityId(projectDetails.project.id)
            4 -> editProjectView.editProject(projectDetails.project.id)
            5 -> deleteProjectView.deleteProject(projectDetails.project.id)
            0 -> {
                cliPrinter.cliPrintLn("\nExiting Project..."); return
            }
        }
        start(projectDetails.project.id, loggedInUserType)
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            if (loggedInUserType == User.Type.ADMIN) MAX_OPTION_NUMBER_ADMIN
            else MAX_OPTION_NUMBER_MATE

        return cliReader.getValidInputNumberInRange(maxOptionNumberAllowed)
    }

    private suspend fun selectTask() {
        if (projectDetails.tasks.isEmpty()) {
            cliPrinter.cliPrintLn("No tasks available to select.")
            return
        }
        cliPrinter.cliPrintLn("Select a task by number:")
        val input = cliReader.getValidInputNumberInRange(projectDetails.tasks.size)
        taskManagementView.start(projectDetails.tasks[input - 1].id, projectDetails.project.id)
    }

    private suspend fun createNewTask() {
        val title = taskInputReader.getValidTaskTitle()
        val description = taskInputReader.getValidTaskDescription()

        makeRequest(
            request = { manageTaskUseCase.addNewTask(title, description, projectDetails.project.id) },
            onSuccess = { cliPrinter.cliPrintLn("Task (${title}) has been created successfully.") },
            onLoadingMessage = "Creating task..."
        )
    }

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 5
        const val MAX_OPTION_NUMBER_MATE = 3
    }
}