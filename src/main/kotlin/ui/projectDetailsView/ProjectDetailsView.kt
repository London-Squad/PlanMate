package ui.projectDetailsView

import logic.entities.Project
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.useCases.GetLoggedInUserUseCase
import logic.useCases.ProjectUseCases
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.logsView.LogsView

class ProjectDetailsView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val swimlanesView: SwimlanesView,
    private val editProjectView: EditProjectView,
    private val deleteProjectView: DeleteProjectView,
    private val projectTasksView: ProjectTasksView,
    private val projectUseCases: ProjectUseCases,
    private val logsView: LogsView,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    private lateinit var currentProject: Project

    fun start(project: Project) {
        currentProject = project

        try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (e: NoLoggedInUserIsSavedInCacheException) {
            cliPrinter.cliPrintLn(ERROR_MESSAGE)
            return
        }

        swimlanesView.displaySwimlanes(currentProject)
        printProjectMenu()
        handleUserInput()
    }

    private fun printProjectMenu() {
        val currentUser = getLoggedInUserUseCase.getLoggedInUser()
        cliPrinter.cliPrintLn("1. Manage tasks")
        cliPrinter.cliPrintLn("2. View project logs")
        if (currentUser.type == User.Type.ADMIN) {
            cliPrinter.cliPrintLn("3. Edit project")
            cliPrinter.cliPrintLn("4. Delete project")
        }
        cliPrinter.cliPrintLn("0. Back to projects")
    }

    private fun handleUserInput() {
        val currentUser = getLoggedInUserUseCase.getLoggedInUser()
        val maxVisibleOptionNumber = if (currentUser.type == User.Type.ADMIN) MAX_OPTION_NUMBER_ADMIN
        else MAX_OPTION_NUMBER_MATE
        val input = cliReader.getValidUserNumberInRange(maxVisibleOptionNumber)

        when (input) {
            "1" -> {
                currentProject = projectTasksView.manageTasks(currentProject)
                currentProject = projectUseCases.getProjectById(currentProject.id) ?: currentProject
                printProjectMenu()
                handleUserInput()
            }

            "2" -> {
                viewProjectLogs()
                printProjectMenu()
                handleUserInput()
            }

            "3" -> if (currentUser.type == User.Type.ADMIN) {
                currentProject = editProjectView.editProject(currentProject)
                viewExceptionHandler.tryCall {
                    currentProject =
                        projectUseCases.getProjectById(currentProject.id) ?: currentProject
                }
                printProjectMenu()
                handleUserInput()
            } else return

            "4" -> {
                if (currentUser.type == User.Type.ADMIN) {
                    deleteProjectView.deleteProject(currentProject)
                    return
                } else return
            }

            "0" -> return
        }
        start(currentProject)
    }

    private fun viewProjectLogs() {
        logsView.printLogsByEntityId(currentProject.id)
    }

    companion object {
        const val ERROR_MESSAGE = "Error: No project selected or user not logged in."
        const val MAX_OPTION_NUMBER_ADMIN = 4
        const val MAX_OPTION_NUMBER_MATE = 2
    }
}