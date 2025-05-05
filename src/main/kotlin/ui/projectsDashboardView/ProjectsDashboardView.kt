package ui.projectsDashboardView

import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.useCases.GetLoggedInUserUseCase
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.cliPrintersAndReaders.cliTable.InvalidTableInput
import ui.projectDetailsView.ProjectDetailsView

class ProjectsDashboardView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val projectView: ProjectDetailsView,
    private val cliTablePrinter: CLITablePrinter = CLITablePrinter(cliPrinter),
) {

    lateinit var currentUser: User

    fun start() {

        try {
            currentUser = getLoggedInUserUseCase.getLoggedInUser()
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
            val headers = listOf("Project #", "Title", "Description")
            val data = projects.mapIndexed { index, project ->
                listOf(
                    (index + 1).toString(), project.title, project.description
                )
            }
            val columnsWidth = listOf(null, null, null)
            try {
                cliTablePrinter(headers, data, columnsWidth)
            } catch (e: InvalidTableInput) {
                cliPrinter.cliPrintLn("Error displaying swimlanes: ${e.message}")
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
        val title = cliReader.getValidTitle()
        val description = cliReader.getValidDescription()


        projectUseCases.createProject(title, description)
        cliPrinter.cliPrintLn("Project created successfully.")
    }
}