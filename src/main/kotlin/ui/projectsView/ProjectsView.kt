package ui.projectsView

import logic.entities.User
import logic.useCases.ProjectUseCases
import main.logic.useCases.LogUseCases
import main.logic.useCases.StateUseCases
import main.logic.useCases.TaskUseCases
import org.koin.core.parameter.parametersOf

import org.koin.java.KoinJavaComponent.getKoin
import ui.View
import ui.cLIPrintersAndReaders.CLIPrinter
import ui.cLIPrintersAndReaders.CLIReader
import ui.projectView.ProjectView
import java.util.UUID

class ProjectsView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val taskUseCases: TaskUseCases,
    private val stateUseCases: StateUseCases,
    private val logUseCases: LogUseCases,
    private val currentUser: User
) : View {

    override fun start() {
        printProjectsMenu()
        handleUserInput()
    }

    private fun printProjectsMenu() {
        cliPrinter.printHeader("Projects Menu")
        cliPrinter.cliPrintLn("1. View projects")
        if (currentUser.type == User.Type.ADMIN) {
            cliPrinter.cliPrintLn("2. Create new project")
        }
        cliPrinter.cliPrintLn("0. Back to main menu")
    }

    private fun handleUserInput() {
        val validInputs = if (currentUser.type == User.Type.ADMIN) listOf("0", "1", "2") else listOf("0", "1")
        val input = cliReader.getValidUserInput(
            isValidInput = { it in validInputs },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )
        when (input) {
            "1" -> viewProjects()
            "2" -> if (currentUser.type == User.Type.ADMIN) createProject() else return
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
        projects.forEach { project ->
            cliPrinter.cliPrintLn("ID: ${project.id}")
            cliPrinter.cliPrintLn("Title: ${project.title}")
            cliPrinter.cliPrintLn("Description: ${project.description}")
            cliPrinter.cliPrintLn(cliPrinter.getThinHorizontal())
        }

        cliPrinter.cliPrintLn("Enter the ID of the project to select (or 'back' to return):")
        val input = cliReader.getUserInput("Project ID: ").trim()
        if (input.lowercase() == "back") return

        val projectId = try {
            UUID.fromString(input)
        } catch (e: IllegalArgumentException) {
            cliPrinter.cliPrintLn("Invalid UUID format.")
            return
        }

        val project = projectUseCases.getProjectById(projectId)
        if (project == null) {
            cliPrinter.cliPrintLn("Project not found.")
            return
        }

        val projectView: ProjectView = getKoin().get { parametersOf(project, currentUser) }
        projectView.start()
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