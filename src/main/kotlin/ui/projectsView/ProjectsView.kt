package ui.projectsView

import logic.entities.Project
import logic.entities.User
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.projectView.ProjectView

class ProjectsView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val cacheDataRepository: CacheDataRepository,
    private val projectView: ProjectView
) {

    fun start() {
        val currentUser = cacheDataRepository.getLoggedInUser()
        if (currentUser == null) {
            cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.")
            return
        }
        handleProjectsView(currentUser)
    }

     fun handleProjectsView(currentUser: User) {
        while (true) {
            handleProjectSelection(currentUser)
            printProjectsMenu()
            if (!handleMenuInput()) break
        }
    }

     fun printProjectsMenu() {
        cliPrinter.printHeader("Projects Menu")
        cliPrinter.cliPrintLn("0. Back to main menu")
    }

     private fun handleMenuInput(): Boolean {
        val validInputs = listOf("0")
        val input = cliReader.getValidUserInput(
            isValidInput = { it in validInputs },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )
        return input != "0"
    }

     fun displayProjects():List<Project> {
        val projects = projectUseCases.getAllProjects()
        if (projects.isEmpty()) {
            cliPrinter.cliPrintLn("No projects found.")
        } else {
            cliPrinter.printHeader("Available Projects")
            projects.forEachIndexed { index, project ->
                val displayIndex = index + 1
                cliPrinter.cliPrintLn("Project: $displayIndex")
                cliPrinter.cliPrintLn("Title: ${project.title}")
                cliPrinter.cliPrintLn("Description: ${project.description}")
                cliPrinter.cliPrintLn(cliPrinter.getThinHorizontal())
            }
        }
        return projects
    }

     fun handleProjectSelection(currentUser: User) {
        while (true) {
            val projects = displayProjects()
            val promptMessage = if (currentUser.type == User.Type.ADMIN) {
                "Enter the project number to select, 'new' to create a new project, or 'back' to return:"
            } else {
                "Enter the project number to select or 'back' to return:"
            }
            cliPrinter.cliPrintLn(promptMessage)
            val input = cliReader.getUserInput("Choice: ").trim().lowercase()

            when (input) {
                "back" -> return
                "new" -> handleNewProject(currentUser)
                else -> handleProjectSelectionInput(input, projects)
            }
        }
    }

     fun handleNewProject(currentUser: User) {
        if (currentUser.type == User.Type.ADMIN) {
            createProject()
        } else {
            cliPrinter.cliPrintLn("Invalid option. Please enter a project number or 'back'.")
        }
    }

     fun handleProjectSelectionInput(input: String, projects: List<Project>) {
        val projectIndex = try {
            val number = input.toInt()
            if (number in 1..projects.size) number - 1 else null
        } catch (e: NumberFormatException) {
            null
        }

        if (projectIndex == null) {
            cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and ${projects.size}, or 'back'.")
            return
        }

        val project = projects[projectIndex]
        projectView.start(project)
    }

     fun createProject() {
        cliPrinter.printHeader("Create Project")
        val title = cliReader.getValidUserInput(
            message = "Enter project title: ",
            invalidInputMessage = "Title cannot be empty",
            isValidInput = { it.isNotBlank() }
        )
        val description = cliReader.getUserInput(
            message = "Enter project description: ",
        )

        projectUseCases.createProject(title, description)
        cliPrinter.cliPrintLn("Project created successfully.")
    }
}