package ui.projectsDashboardView

import logic.entities.Project
import logic.entities.User
import logic.useCases.CreateProjectUseCase
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.ProjectInputReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.projectDetailsView.ProjectDetailsView

class ProjectsDashboardView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectInputReader: ProjectInputReader,
    private val manageProjectUseCase: ManageProjectUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val projectView: ProjectDetailsView,
    private val exceptionHandler: ViewExceptionHandler,
    private val cliTablePrinter: CLITablePrinter
) {
    private lateinit var loggedInUserType: User.Type
    private var currentViewState: ViewState<List<Project>> = ViewState.Loading

    suspend fun start(loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType
        printHeader()
        loadProjects()
    }

    private fun printHeader() {
        cliPrinter.printHeader("Projects Dashboard Menu")
    }

    private suspend fun loadProjects() {
        exceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading projects...")
                currentViewState = ViewState.Loading
            },
            onSuccess = { projects ->
                currentViewState = ViewState.Success(projects)
                printProjects(projects)
                printOptions()
                goToNextView(projects)
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                printLn("Failed to load projects: ${exception.message}")
                printOptions()
                goToNextView(emptyList())
            },
            operation = {
                manageProjectUseCase.getAllProjects()
            }
        )
    }

    private fun printProjects(projects: List<Project>) {
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
        printLn("1. Select a project")
        // TODO : add option to view all logs for all projects at once
        if (loggedInUserType == User.Type.ADMIN) printLn("2. Create a new project")
        printLn("0. Exit Projects Dashboard")
    }

    private suspend fun goToNextView(projects: List<Project>) {
        when (getValidUserInput()) {
            1 -> selectProject(projects)
            2 -> createProject()
            0 -> {
                printLn("\nExiting Projects Dashboard ...")
                return
            }
        }
        start(loggedInUserType)
    }

    private fun getValidUserInput(): Int {
        val maxOptionNumberAllowed =
            if (loggedInUserType == User.Type.ADMIN) MAX_OPTION_NUMBER_ADMIN
            else MAX_OPTION_NUMBER_MATE

        return cliReader.getValidInputNumberInRange(min = 0, max = maxOptionNumberAllowed)
    }

    private suspend fun selectProject(projects: List<Project>) {
        if (projects.isEmpty()) {
            printLn("No projects available to select.")
            return
        }
        printLn("Select a project by number:")
        val input = cliReader.getValidInputNumberInRange(min = 1, max = projects.size)
        projectView.start(projects[input - 1].id, loggedInUserType)
    }

    private suspend fun createProject() {
        cliPrinter.printHeader("Create Project")
        val title = projectInputReader.getValidProjectTitle()
        val description = projectInputReader.getValidProjectDescription()

        exceptionHandler.executeWithState(
            onLoading = {
                printLn("Creating project...")
                currentViewState = ViewState.Loading
            },
            onSuccess = {
                currentViewState = ViewState.Success(emptyList())
                printLn("Project created successfully.")
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                printLn("Failed to create project: ${exception.message}")
            },
            operation = {
                createProjectUseCase.createProject(title, description)
            }
        )
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 2
        const val MAX_OPTION_NUMBER_MATE = 1
    }
}