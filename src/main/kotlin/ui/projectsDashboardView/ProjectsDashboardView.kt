package ui.projectsDashboardView

import logic.entities.User
import logic.exceptions.*
import logic.useCases.GetLoggedInUserUseCase
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.projectDetailsView.ProjectDetailsView
import ui.ViewExceptionHandler

class ProjectsDashboardView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val projectView: ProjectDetailsView,
    private val exceptionHandler: ViewExceptionHandler
) {
    fun start() {
        exceptionHandler.tryCall {
            getLoggedInUserUseCase.getLoggedInUser()
                .let { user ->
                    printHeader()
                    printProjects()
                    goToNextUi(user)
                }
        }
    }

    private fun printHeader() {
        cliPrinter.printHeader("Projects Menu")
    }

    private fun printProjects() {
        exceptionHandler.tryCall {
            projectUseCases.getAllProjects()
                .takeIf { it.isNotEmpty() }
                ?.forEachIndexed { index, project ->
                    val displayIndex = index + 1
                    cliPrinter.cliPrintLn("Project: $displayIndex")
                    cliPrinter.cliPrintLn("Title: ${project.title}")
                    cliPrinter.cliPrintLn("Description: ${project.description}")
                    cliPrinter.cliPrintLn(cliPrinter.getThinHorizontal())
                } ?: cliPrinter.cliPrintLn("No projects found.")
        }
    }

    private fun goToNextUi(user: User) {
        printInputInstruction(user)
        handleProjectSelection(user)
    }

    private fun printInputInstruction(user: User) {
        val promptMessage = if (user.type == User.Type.ADMIN) {
            "Enter the project number to select, 'new' to create a new project, or 'back' to return:"
        } else {
            "Enter the project number to select or 'back' to return: "
        }
        cliPrinter.cliPrintLn(promptMessage)
    }

    private fun handleProjectSelection(user: User) {
        exceptionHandler.tryCall {
            cliReader.getUserInput("Choice: ").trim().lowercase()
                .let { input ->
                    when (input) {
                        "back" -> Unit
                        "new" -> handleNewProject(user)
                        else -> handleProjectSelectionInput(input)
                    }
                }
                .takeUnless { it == Unit }
                ?.let { start() }
        }
    }

    private fun handleNewProject(user: User) {
        if (user.type == User.Type.ADMIN) {
            createProject()
        } else {
            cliPrinter.cliPrintLn("Invalid option. Please enter a project number or 'back'.")
        }
    }

    private fun handleProjectSelectionInput(input: String) {
        exceptionHandler.tryCall {
            val projects = projectUseCases.getAllProjects()
            input.toInt()
                .takeIf { it in 1..projects.size }
                ?.let { number -> projects[number - 1] }
                ?.let { project -> projectView.start(project) }
                ?: throw NotFoundException("Invalid project number.")
        }
    }

    private fun createProject() {
        exceptionHandler.tryCall {
            cliPrinter.printHeader("Create Project")
            val title = cliReader.getValidTitle()
            val description = cliReader.getValidDescription()
            projectUseCases.createProject(title, description)
                .also { cliPrinter.cliPrintLn("Project created successfully.") }
        }
    }
}