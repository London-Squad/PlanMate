package ui.projectsView

import logic.useCases.ProjectUseCases
import ui.View
import ui.cLIPrintersAndReaders.CLIPrinter
import ui.cLIPrintersAndReaders.CLIReader
import ui.projectView.ProjectView
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

        cliPrinter.cliPrintLn("Enter the ID of the project to view its swimlanes:")
        val projectId = getValidProjectId()
        val project = projectUseCases.getProjectById(projectId)
        if (project == null) {
            cliPrinter.cliPrintLn("Project not found.")
            return
        }

        val projectView = ProjectView(project, cliPrinter, cliReader)
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

    private fun editProjectTitle() {
        val projectId = getValidProjectId()
        val newTitle = cliReader.getValidUserInput(
            message = "Enter new project title: ",
            invalidInputMessage = "Title cannot be empty",
            isValidInput = { it.isNotBlank() }
        )

        projectUseCases.editProjectTitle(projectId, newTitle)
        cliPrinter.cliPrintLn("Project title updated.")
    }

    private fun editProjectDescription() {
        val projectId = getValidProjectId()
        val newDescription = cliReader.getValidUserInput(
            message = "Enter new project description: ",
            invalidInputMessage = "Description cannot be empty",
            isValidInput = { it.isNotBlank() }
        )

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
                }
            )
            try {
                val projectId = UUID.fromString(projectIdStr)
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
}