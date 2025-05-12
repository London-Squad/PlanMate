package ui.projectsDashboardView

import logic.entities.Project
import logic.entities.User
import logic.useCases.CreateProjectUseCase
import logic.useCases.ManageProjectUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.CLITablePrinter
import ui.cliPrintersAndReaders.ProjectInputReader
import ui.projectDetailsView.ProjectDetailsView

class ProjectsDashboardView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectInputReader: ProjectInputReader,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val projectView: ProjectDetailsView,
    private val cliTablePrinter: CLITablePrinter
) : BaseView(cliPrinter) {

    private lateinit var loggedInUserType: User.Type
    private var projects: List<Project> = emptyList()

    fun start(loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType
        makeRequest(
            request = { fetchProjects() },
            onSuccess = {
                printHeader()
                printProjects()
                printOptions()
                goToNextView()
            },
            onLoadingMessage = "Fetching projects..."
        )

    }

    private fun printHeader() {
        cliPrinter.printHeader("Projects Dashboard Menu")
    }

    private suspend fun fetchProjects() {
        projects = manageProjectUseCase.getAllProjects()
    }

    private fun printProjects() {

        if (projects.isEmpty()) {
            cliPrinter.cliPrintLn("No projects found.")
            return
        }

        val headers = listOf("#", "Title", "Description")
        val data = projects.mapIndexed { index, project ->
            listOf(
                (index + 1).toString(), project.title, project.description
            )
        }
        val columnsWidth = listOf(5, 30, null)

        cliTablePrinter(headers, data, columnsWidth)
    }

    private fun printOptions() {
        cliPrinter.cliPrintLn("1. select a project")
        // TODO : add option to view all logs for all projects at once
        if (loggedInUserType == User.Type.ADMIN) cliPrinter.cliPrintLn("2. create a new project")
        cliPrinter.cliPrintLn("0. Exit Projects Dashboard")
    }

    private fun goToNextView() {

        when (getValidUserInput()) {
            1 -> selectProject()
            2 -> createProject()
            0 -> {
                cliPrinter.cliPrintLn("\nExiting Projects Dashboard ..."); return
            }
        }
        start(loggedInUserType)
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            if (loggedInUserType == User.Type.ADMIN) MAX_OPTION_NUMBER_ADMIN
            else MAX_OPTION_NUMBER_MATE

        return cliReader.getValidInputNumberInRange(maxOptionNumberAllowed)
    }

    private fun selectProject() {
        if (projects.isEmpty()) {
            cliPrinter.cliPrintLn("No projects available to select.")
            return
        }
        cliPrinter.cliPrintLn("Select a project by number:")
        val input = cliReader.getValidInputNumberInRange(projects.size)
        projectView.start(projects[input - 1].id, loggedInUserType)
    }

    private fun createProject() {
        cliPrinter.printHeader("Create Project")
        val title = projectInputReader.getValidProjectTitle()
        val description = projectInputReader.getValidProjectDescription()
        makeRequest(
            request = { createProjectUseCase.createProject(title, description) },
            onSuccess = { cliPrinter.cliPrintLn("Project (${title}) have been created successfully.") },
            onLoadingMessage = "Creating project..."
        )
    }

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 2
        const val MAX_OPTION_NUMBER_MATE = 1
    }
}