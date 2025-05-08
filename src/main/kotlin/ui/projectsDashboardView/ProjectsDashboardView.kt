package ui.projectsDashboardView

import logic.entities.Project
import logic.entities.User
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.projectDetailsView.ProjectDetailsView
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.ProjectInputReader

class ProjectsDashboardView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectInputReader: ProjectInputReader,
    private val projectUseCases: ProjectUseCases,
    private val projectView: ProjectDetailsView,
    private val exceptionHandler: ViewExceptionHandler,
    private val cliTablePrinter: CLITablePrinter = CLITablePrinter(cliPrinter),
) {

    private lateinit var loggedInUserType: User.Type
    private var projects: List<Project> = emptyList()

    fun start(loggedInUserType: User.Type) {
        this.loggedInUserType = loggedInUserType
        printHeader()
        getProjects()
        printProjects()
        printOptions()
        goToNextView()
    }

    private fun printHeader() {
        cliPrinter.printHeader("Projects Dashboard Menu")
    }

    private fun getProjects() {
        exceptionHandler.tryCall {
            projects = projectUseCases.getAllProjects()
        }.also { if (!it) return }
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
        printLn("1. select a project")
        // TODO : add option to view all logs for all projects at once
        if (loggedInUserType == User.Type.ADMIN) printLn("2. create a new project")
        printLn("0. Exit Projects Dashboard")
    }

    private fun goToNextView() {

        when (getValidUserInput()) {
            1 -> selectProject()
            2 -> createProject()
            0 -> {
                printLn("\nExiting Projects Dashboard ..."); return
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
            printLn("No projects available to select.")
            return
        }
        printLn("Select a project by number:")
        val input = cliReader.getValidInputNumberInRange(projects.size)
        projectView.start(projects[input - 1].id, loggedInUserType)
    }

    private fun createProject() {
        cliPrinter.printHeader("Create Project")
        val title = projectInputReader.getValidProjectTitle()
        val description = projectInputReader.getValidProjectDescription()
        exceptionHandler.tryCall {
            projectUseCases.createProject(title, description)
            cliPrinter.cliPrintLn("Project created successfully.")
        }
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)

    private companion object {
        const val MAX_OPTION_NUMBER_ADMIN = 2
        const val MAX_OPTION_NUMBER_MATE = 1
    }
}