package ui.projectsView

import logic.useCases.ProjectUseCases
import ui.cLIPrintersAndReaders.CLIPrinter
import ui.cLIPrintersAndReaders.CLIReader
import ui.View
import java.util.UUID

class ProjectsView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases
) : View {

    override fun start() {
        printProjectsMenu()
        handleUserInput()
    }

    private fun printProjectsMenu() {
        cliPrinter.printHeader("Projects Menu")
        cliPrinter.cliPrintLn("1. View all projects")
        cliPrinter.cliPrintLn("2. View project swimlanes")
        cliPrinter.cliPrintLn("3. Create new project")
        cliPrinter.cliPrintLn("4. Edit project title")
        cliPrinter.cliPrintLn("5. Edit project description")
        cliPrinter.cliPrintLn("6. Delete project")
        cliPrinter.cliPrintLn("0. Back to main menu")
    }

    private fun handleUserInput() {
        when (getValidUserInput()) {
            "1" -> viewAllProjects()
            "2" -> viewProjectSwimlanes()
            "3" -> createProject()
            "4" -> editProjectTitle()
            "5" -> editProjectDescription()
            "6" -> deleteProject()
            "0" -> return
        }
        start()
    }

    private fun getValidUserInput(): String {
        val validInputs = listOf("0", "1", "2", "3", "4", "5", "6")
        return cliReader.getValidUserInput(
            isValidInput = { it in validInputs },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )
    }

    private fun viewAllProjects() {
        val projects = projectUseCases.getAllProjects()
        if (projects.isEmpty()) {
            cliPrinter.cliPrintLn("No projects found.")
            return
        }

        cliPrinter.printHeader("All Projects")
        projects.forEach { project ->
            cliPrinter.cliPrintLn("ID: ${project.id}")
            cliPrinter.cliPrintLn("Title: ${project.title}")
            cliPrinter.cliPrintLn("Description: ${project.description}")
            cliPrinter.cliPrintLn(cliPrinter.getThinHorizontal())
        }
    }

    private fun viewProjectSwimlanes() {
        val projectId = getValidProjectId()
        val project = projectUseCases.getProjectById(projectId)
        if (project == null) {
            cliPrinter.cliPrintLn("Project not found.")
            return
        }

        cliPrinter.printHeader("Swimlanes: ${project.title}")
        if (project.states.isEmpty()) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        // Group tasks by state
        val tasksByState = project.states.associateWith { state ->
            project.tasks.filter { it.state.id == state.id }
        }

        // Calculate the maximum number of tasks in any state for row alignment
        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        val columnWidth = 30 // Width for each state column
        val separator = "|"

        // Print state headers
        project.states.forEach { state ->
            val stateHeader = state.title.padEnd(columnWidth - 2).take(columnWidth - 2)
            cliPrinter.cliPrint(" $stateHeader $separator")
        }
        cliPrinter.cliPrintLn("")

        // Print separator line
        project.states.forEach {
            cliPrinter.cliPrint("-".duplicate(columnWidth) + separator)
        }
        cliPrinter.cliPrintLn("")

        // Print tasks row by row
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

        cliPrinter.cliPrintLn("\nSelect a task to view details (enter task ID or 'back' to return): ")
        val input = cliReader.getUserInput("Task ID: ").trim()
        if (input.lowercase() == "back") return

        try {
            val taskId = UUID.fromString(input)
            val task = project.tasks.find { it.id == taskId }
            if (task != null) {
                cliPrinter.cliPrintLn("\nTask Details:")
                cliPrinter.cliPrintLn("ID: ${task.id}")
                cliPrinter.cliPrintLn("Title: ${task.title}")
                cliPrinter.cliPrintLn("Description: ${task.description}")
                cliPrinter.cliPrintLn("State: ${task.state.title}")
            } else {
                cliPrinter.cliPrintLn("Task not found.")
            }
        } catch (e: IllegalArgumentException) {
            cliPrinter.cliPrintLn("Invalid task ID format.")
        }
    }

    private fun createProject() {
        cliPrinter.printHeader("Create Project")
        val title = cliReader.getValidUserInput(
            message = "Enter project title: ",
            invalidInputMessage = "Title cannot be empty",
            isValidInput = { it.isNotBlank() })
        val description = cliReader.getValidUserInput(
            message = "Enter project description: ",
            invalidInputMessage = "Description cannot be empty",
            isValidInput = { it.isNotBlank() })

        projectUseCases.createProject(title, description)
        cliPrinter.cliPrintLn("Project created successfully.")
    }

    private fun editProjectTitle() {
        val projectId = getValidProjectId()
        val newTitle = cliReader.getValidUserInput(
            message = "Enter new project title: ",
            invalidInputMessage = "Title cannot be empty",
            isValidInput = { it.isNotBlank() })

        projectUseCases.editProjectTitle(projectId, newTitle)
        cliPrinter.cliPrintLn("Project title updated.")
    }

    private fun editProjectDescription() {
        val projectId = getValidProjectId()
        val newDescription = cliReader.getValidUserInput(
            message = "Enter new project description: ",
            invalidInputMessage = "Description cannot be empty",
            isValidInput = { it.isNotBlank() })

        projectUseCases.editProjectDescription(projectId, newDescription)
        cliPrinter.cliPrintLn("Project description updated.")
    }

    private fun deleteProject() {
        val projectId = getValidProjectId()
        projectUseCases.deleteProject(projectId)
        cliPrinter.cliPrintLn("Project deleted.")
    }

    private fun getValidProjectId(): UUID {
        while (true) {
            val projectIdStr = cliReader.getValidUserInput(
                message = "Enter project ID: ",
                invalidInputMessage = "Invalid UUID format, try again ...",
                isValidInput = { input ->
                    try {
                        UUID.fromString(input)
                        true
                    } catch (e: IllegalArgumentException) {
                        false
                    }
                })
            try {
                val projectId = UUID.fromString(projectIdStr)
                // Verify if the project exists
                if (projectUseCases.getProjectById(projectId) != null) {
                    return projectId
                } else {
                    cliPrinter.cliPrintLn("Project not found, try again ...")
                }
            } catch (e: IllegalArgumentException) {
                cliPrinter.cliPrintLn("Invalid UUID format, try again ...")
            }
        }
    }

    private fun String.duplicate(numberOfDuplication: Int) =
        List(numberOfDuplication) { this }.joinToString(separator = "")
}
