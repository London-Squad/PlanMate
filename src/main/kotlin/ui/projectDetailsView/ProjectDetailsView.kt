package ui.projectDetailsView

import logic.entities.User
import logic.useCases.GetProjectDetailsUseCase
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
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
    private val viewExceptionHandler: ViewExceptionHandler,
) {

    private lateinit var loggedInUserType: User.Type
    private lateinit var projectDetails: GetProjectDetailsUseCase.ProjectDetails


    fun start(projectId: UUID, loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType

        viewExceptionHandler.tryCall {
            projectDetails = getProjectDetailsUseCase(projectId)
        }
            .also { if (!it) return }

        printHeader(projectDetails.project.title)
        swimlanesView.displaySwimlanes(projectDetails.tasks, projectDetails.taskStates)

        printOptions()
        goToNextView()
    }

    private fun printHeader(title: String) {
        cliPrinter.printHeader("Project: $title")
    }

    private fun printOptions() {
        printLn("\n1. select a task")
        printLn("2. create new tasks")
        printLn("3. View project logs")
        if (loggedInUserType == User.Type.ADMIN) {
            printLn("4. Edit project")
            printLn("5. Delete project")
        }
        printLn("0. Back to projects")
    }

    private fun goToNextView() {
        when (getValidUserInput()) {
            1 -> selectTask()
            2 -> createNewTask()
            3 -> logsView.printLogsByEntityId(projectDetails.project.id)
            4 -> editProjectView.editProject(projectDetails.project.id)
            5 -> deleteProjectView.deleteProject(projectDetails.project.id)
            0 -> {
                printLn("\nExiting Project..."); return
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

    private fun selectTask() {
        if (projectDetails.tasks.isEmpty()) {
            printLn("No tasks available to select.")
            return
        }
        printLn("Select a task by number:")
        val input = cliReader.getValidInputNumberInRange(projectDetails.tasks.size)
        taskManagementView.start(projectDetails.tasks[input - 1].id, projectDetails.project.id)
    }

    private fun createNewTask() {
        val title = taskInputReader.getValidTaskTitle()
        val description = taskInputReader.getValidTaskDescription()

        manageTaskUseCase.addNewTask(title, description, projectDetails.project.id)
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 5
        const val MAX_OPTION_NUMBER_MATE = 3
    }
}