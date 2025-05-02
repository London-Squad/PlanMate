package ui.projectView

import logic.entities.Project
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.logsView.LogsView

class ProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val cacheDataRepository: CacheDataRepository,
    private val swimlanesView: SwimlanesView,
    private val editProjectView: EditProjectView,
    private val deleteProjectView: DeleteProjectView,
    private val projectTasksView: ProjectTasksView,
    private val projectUseCases: ProjectUseCases,
    private val logsView: LogsView
) {

    private lateinit var currentProject: Project

    fun start(project: Project) {
        currentProject = project

        try {
            cacheDataRepository.getLoggedInUser()
        } catch (e: NoLoggedInUserIsSavedInCacheException) {
            cliPrinter.cliPrintLn(ERROR_MESSAGE)
            return
        }

        swimlanesView.displaySwimlanes(currentProject)
        printProjectMenu()
        handleUserInput()
    }

    private fun printProjectMenu() {
        val currentUser = cacheDataRepository.getLoggedInUser()
        cliPrinter.cliPrintLn("1. Manage tasks")
        cliPrinter.cliPrintLn("2. View project logs")
        if (currentUser.type == User.Type.ADMIN) {
            cliPrinter.cliPrintLn("3. Edit project")
            cliPrinter.cliPrintLn("4. Delete project")
        }
        cliPrinter.cliPrintLn("0. Back to projects")
    }

    private fun handleUserInput() {
        val currentUser = cacheDataRepository.getLoggedInUser()
        val validInputs = if (currentUser.type == User.Type.ADMIN) listOf(
            "0", "1", "2", "3", "4",
        ) else listOf("0", "1", "2")
        val input = cliReader.getValidUserInput(
            isValidInput = { it in validInputs },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )
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
                currentProject = projectUseCases.getProjectById(currentProject.id) ?: currentProject
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
    }
}