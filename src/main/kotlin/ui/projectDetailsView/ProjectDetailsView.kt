package ui.projectDetailsView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import logic.entities.Project
import logic.entities.User
import logic.useCases.ManageTaskUseCase
import logic.useCases.ManageProjectUseCase
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
    private val swimlanesView: SwimlanesView,
    private val editProjectView: EditProjectView,
    private val deleteProjectView: DeleteProjectView,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val taskInputReader: TaskInputReader,
    private val taskManagementView: TaskManagementView,
    private val logsView: LogsView,
    private val viewExceptionHandler: ViewExceptionHandler,
    private val manageTaskUseCase: ManageTaskUseCase
) {

    private lateinit var loggedInUserType: User.Type
    private lateinit var project: Project

    fun start(projectId: UUID, loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType

        viewExceptionHandler.tryCall { project = manageProjectUseCase.getProjectById(projectId) }
            .also { if (!it) return }

        swimlanesView.displaySwimlanes(project)

        printOptions()
        goToNextView()
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
            3 -> logsView.printLogsByEntityId(project.id)
            4 -> editProjectView.editProject(project)
            5 -> deleteProjectView.deleteProject(project)
            0 -> {
                printLn("\nExiting Project..."); return
            }
        }
        start(project.id, loggedInUserType)
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            if (loggedInUserType == User.Type.ADMIN) MAX_OPTION_NUMBER_ADMIN
            else MAX_OPTION_NUMBER_MATE

        return cliReader.getValidInputNumberInRange(maxOptionNumberAllowed)
    }

    private fun selectTask() {
        if (project.tasks.isEmpty()) {
            printLn("No tasks available to select.")
            return
        }
        printLn("Select a task by number:")
        val input = cliReader.getValidInputNumberInRange(project.tasks.size)
        taskManagementView.start(project.tasks[input - 1].id, project)
    }

    private fun createNewTask() {
        val title = taskInputReader.getValidTaskTitle()
        val description = taskInputReader.getValidTaskDescription()

        CoroutineScope(Dispatchers.IO).launch {
            manageTaskUseCase.addNewTask(title, description, project.id)
        }
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 5
        const val MAX_OPTION_NUMBER_MATE = 3
    }
}