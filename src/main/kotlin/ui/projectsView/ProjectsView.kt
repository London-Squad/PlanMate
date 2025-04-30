package ui.projectsView

import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import main.logic.useCases.LogUseCases
import main.logic.useCases.StateUseCases
import main.logic.useCases.TaskUseCases

import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.projectView.ProjectView
import org.koin.java.KoinJavaComponent.getKoin

class ProjectsView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val taskUseCases: TaskUseCases,
    private val stateUseCases: StateUseCases,
    private val logUseCases: LogUseCases,
    private val cacheDataRepository: CacheDataRepository
) {

    fun start() {
        val currentUser = cacheDataRepository.getLoggedInUser()
        if (currentUser == null) {
            cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.")
            return
        }
        viewProjects()
        printProjectsMenu(currentUser)
        handleUserInput(currentUser)
    }

    private fun printProjectsMenu(currentUser: logic.entities.User) {
        cliPrinter.printHeader("Projects Menu")
        if (currentUser.type == logic.entities.User.Type.ADMIN) {
            cliPrinter.cliPrintLn("1. Create new project")
            cliPrinter.cliPrintLn("0. Back to main menu")
        } else {
            cliPrinter.cliPrintLn("0. Back to main menu")
        }
    }

    private fun handleUserInput(currentUser: logic.entities.User) {
        val validInputs = if (currentUser.type == logic.entities.User.Type.ADMIN) listOf("0", "1") else listOf("0")
        val input = cliReader.getValidUserInput(
            isValidInput = { it in validInputs },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )
        when (input) {
            "1" -> if (currentUser.type == logic.entities.User.Type.ADMIN) createProject() else return
            "0" -> return
        }
        start()
    }

    private fun viewProjects() {
        val projects = projectUseCases.getAllProjects()
        if (projects.isEmpty()) {
            cliPrinter.cliPrintLn("No projects found.")
            return
        }

        cliPrinter.printHeader("Available Projects")
        projects.forEachIndexed { index, project ->
            val displayIndex = index + 1
            cliPrinter.cliPrintLn("Project: $displayIndex")
            cliPrinter.cliPrintLn("Title: ${project.title}")
            cliPrinter.cliPrintLn("Description: ${project.description}")
            cliPrinter.cliPrintLn(cliPrinter.getThinHorizontal())
        }

        cliPrinter.cliPrintLn("Enter the project number to select (or 'back' to return):")
        val input = cliReader.getUserInput("Project number: ").trim()
        if (input.lowercase() == "back") return

        val projectIndex = try {
            val number = input.toInt()
            if (number in 1..projects.size) number - 1 else null
        } catch (e: NumberFormatException) {
            null
        }

        if (projectIndex == null) {
            cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and ${projects.size}.")
            return
        }

        val project = projects[projectIndex]
        val projectView: ProjectView = getKoin().get()
        projectView.start(project)
    }

    private fun createProject() {
        cliPrinter.printHeader("Create Project")
        val title = cliReader.getValidUserInput(
            message = "Enter project title: ",
            invalidInputMessage = "Title cannot be empty",
            isValidInput = { it.isNotBlank() }
        )
        val description = cliReader.getValidUserInput(
            message = "Enter project description: ",
            invalidInputMessage = "Description cannot be empty",
            isValidInput = { it.isNotBlank() }
        )

        projectUseCases.createProject(title, description)
        cliPrinter.cliPrintLn("Project created successfully.")
    }
}