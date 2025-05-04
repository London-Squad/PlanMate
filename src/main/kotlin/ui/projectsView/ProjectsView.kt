package ui.projectsView

import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
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

    lateinit var currentUser: User

    fun start() {

        try {
            currentUser = cacheDataRepository.getLoggedInUser()
        } catch (e: NoLoggedInUserIsSavedInCacheException) {
            cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.")
            return
        }

        printHeader()
        printProjects()
        goToNextUi()

    }

    private fun printHeader() {
        cliPrinter.printHeader("Projects Menu")
    }

    private fun printProjects() {
        val projects = projectUseCases.getAllProjects()
        if (projects.isEmpty()) {
            cliPrinter.cliPrintLn("No projects found.")
        } else {
            projects.forEachIndexed { index, project ->
                val displayIndex = index + 1
                cliPrinter.cliPrintLn("Project: $displayIndex")
                cliPrinter.cliPrintLn("Title: ${project.title}")
                cliPrinter.cliPrintLn("Description: ${project.description}")
                cliPrinter.cliPrintLn(cliPrinter.getThinHorizontal())
            }
        }
    }

    private fun goToNextUi() {
        printInputInstruction()
        handleProjectSelection()
    }

    private fun printInputInstruction() {
        val promptMessage = if (currentUser.type == User.Type.ADMIN) {
            "Enter the project number to select, 'new' to create a new project, or 'back' to return:"
        } else {
            "Enter the project number to select or 'back' to return: "
        }
        cliPrinter.cliPrintLn(promptMessage)
    }

    private fun handleProjectSelection() {

        val input = cliReader.getUserInput("Choice: ").trim().lowercase()

        when (input) {
            "back" -> return
            "new" -> handleNewProject()
            else -> handleProjectSelectionInput(input)
        }
        start()

    }

    fun handleNewProject() {
        if (currentUser.type == User.Type.ADMIN) {
            createProject()
        } else {
            cliPrinter.cliPrintLn("Invalid option. Please enter a project number or 'back'.")
        }
    }

    fun handleProjectSelectionInput(input: String) {

        val projects = projectUseCases.getAllProjects()

        val projectIndex = try {
            val number = input.toInt()
            if (number in 1..projects.size) number - 1 else null
        } catch (e: NumberFormatException) {
            null
        }

        if (projectIndex == null) {
            cliPrinter.cliPrintLn("Invalid project number.")
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