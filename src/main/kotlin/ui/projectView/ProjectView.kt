package ui.projectView

import logic.entities.Project
import logic.entities.User
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import main.logic.useCases.LogUseCases
import main.logic.useCases.StateUseCases
import main.logic.useCases.TaskUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class ProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val taskUseCases: TaskUseCases,
    private val stateUseCases: StateUseCases,
    private val logUseCases: LogUseCases,
    private val cacheDataRepository: CacheDataRepository
) {

    private lateinit var currentProject: Project

    fun start(project: Project) {
        currentProject = project
        val currentUser = cacheDataRepository.getLoggedInUser()
        if (currentUser == null) {
            cliPrinter.cliPrintLn("Error: No project selected or user not logged in.")
            return
        }

        displaySwimlanes(currentProject)
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
            "4" -> if (currentUser?.type == User.Type.ADMIN) editProject() else return
            "5" -> {
                if (currentUser?.type == User.Type.ADMIN) {
                    deleteProject(currentProject)
                } else return
            }
            "0" -> return
        }
        start(currentProject)
    }

    private fun displaySwimlanes(project: Project) {
        cliPrinter.printHeader("Swimlanes: ${project.title}")
        if (project.states.isEmpty()) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        val tasksByState = project.states.associateWith { state ->
            project.tasks.filter { it.state.id == state.id }
        }

        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        val columnWidth = 30
        val separator = "|"

        project.states.forEach { state ->
            val stateHeader = state.title.padEnd(columnWidth - 2).take(columnWidth - 2)
            cliPrinter.cliPrint(" $stateHeader $separator")
        }
        cliPrinter.cliPrintLn("")

        project.states.forEach {
            cliPrinter.cliPrint("-".duplicate(columnWidth) + separator)
        }
        cliPrinter.cliPrintLn("")

        for (row in 0 until maxTasks) {
            project.states.forEach { state ->
                val tasks = tasksByState[state] ?: emptyList()
                if (row < tasks.size) {
                    val task = tasks[row]
                    val taskTitle = task.title.take(columnWidth - 4).padEnd(columnWidth - 4)
                    cliPrinter.cliPrint(" $taskTitle $separator")
                } else {
                    cliPrinter.cliPrint("".padEnd(columnWidth) + separator)
                }
            }
            cliPrinter.cliPrintLn("")
        }
    }

    private fun editProject() {
        cliPrinter.printHeader("Edit Project")
        cliPrinter.cliPrintLn("1. Edit title")
        cliPrinter.cliPrintLn("2. Edit description")
        cliPrinter.cliPrintLn("3. States management")
        cliPrinter.cliPrintLn("0. Back to project")

        val input = cliReader.getValidUserInput(
            isValidInput = { it in listOf("0", "1", "2", "3") },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )

        when (input) {
            "1" -> editProjectTitle()
            "2" -> editProjectDescription()
            "3" -> statesManagement()
            "0" -> return
        }
        editProject()
    }

    private fun editProjectTitle() {
        val newTitle = cliReader.getValidUserInput(
            message = "Enter new project title: ",
            invalidInputMessage = "Title cannot be empty",
            isValidInput = { it.isNotBlank() }
        )
        projectUseCases.editProjectTitle(currentProject.id, newTitle)
        // Update the currentProject with the new title
        currentProject = currentProject.copy(title = newTitle)
        cliPrinter.cliPrintLn("Project title updated.")
    }

    private fun editProjectDescription() {
        val newDescription = cliReader.getValidUserInput(
            message = "Enter new project description: ",
            invalidInputMessage = "Description cannot be empty",
            isValidInput = { it.isNotBlank() }
        )
        projectUseCases.editProjectDescription(currentProject.id, newDescription)
        // Update the currentProject with the new description
        currentProject = currentProject.copy(description = newDescription)
        cliPrinter.cliPrintLn("Project description updated.")
    }

    private fun deleteProject(project: Project) {
        projectUseCases.deleteProject(project.id)
        cliPrinter.cliPrintLn("Project deleted.")
    }

    private fun String.duplicate(numberOfDuplication: Int) =
        List(numberOfDuplication) { this }.joinToString(separator = "")

    private fun addNewTask() {
        /**
         * To-Do
         */
    }

    private fun selectTask() {
        /**
         * To-Do
         */
    }

    private fun viewProjectLogs() {
        /**
         * To-Do
         */
    }

    private fun statesManagement() {
        /**
         * To-Do
         */
    }
}