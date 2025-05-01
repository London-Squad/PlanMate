package ui.projectView

import logic.entities.Project
import logic.entities.User
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class ProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val cacheDataRepository: CacheDataRepository,
    private val swimlanesView: SwimlanesView,
    private val editProjectView: EditProjectView,
    private val deleteProjectView: DeleteProjectView
) {

    private lateinit var currentProject: Project

    fun start(project: Project) {
        currentProject = project
        val currentUser = cacheDataRepository.getLoggedInUser()
        if (currentUser == null) {
            cliPrinter.cliPrintLn(ERROR_MESSAGE)
            return
        }

        swimlanesView.displaySwimlanes(currentProject)
        printProjectMenu()
        handleUserInput()
    }

    private fun printProjectMenu() {
        val currentUser = cacheDataRepository.getLoggedInUser()
        cliPrinter.printHeader("Project: ${currentProject.title}")
        cliPrinter.cliPrintLn("1. Add new task")
        cliPrinter.cliPrintLn("2. Select task")
        cliPrinter.cliPrintLn("3. View project logs")
        if (currentUser?.type == User.Type.ADMIN) {
            cliPrinter.cliPrintLn("4. Edit project")
            cliPrinter.cliPrintLn("5. Delete project")
        }
        cliPrinter.cliPrintLn("0. Back to projects")
    }

    private fun handleUserInput() {
        val currentUser = cacheDataRepository.getLoggedInUser()
        val validInputs = if (currentUser?.type == User.Type.ADMIN) listOf(
            "0", "1", "2", "3", "4", "5",
        ) else listOf("0", "1", "2", "3")
        val input = cliReader.getValidUserInput(
            isValidInput = { it in validInputs },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )
        when (input) {
            "1" -> addNewTask()
            "2" -> selectTask()
            "3" -> viewProjectLogs()
            "4" -> if (currentUser?.type == User.Type.ADMIN) editProjectView.editProject(currentProject) else return
            "5" -> {
                if (currentUser?.type == User.Type.ADMIN) {
                    deleteProjectView.deleteProject(currentProject)
                } else return
            }
            "0" -> return
        }
        start(currentProject)
    }

    private fun addNewTask() {
        TODO("Not yet implemented")
    }

    private fun selectTask() {
        TODO("Not yet implemented")
    }

    private fun viewProjectLogs() {
        TODO("Not yet implemented")
    }

    companion object {
        const val ERROR_MESSAGE = "Error: No project selected or user not logged in."
    }
}