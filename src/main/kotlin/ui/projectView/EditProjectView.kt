package ui.projectView

import logic.entities.Project
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class EditProjectView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases
) {

    lateinit var currentProject: Project

    fun editProject(project: Project): Project {
        currentProject = project
        cliPrinter.printHeader("Edit Project: ${currentProject.title}")
        cliPrinter.cliPrintLn("1. Edit title")
        cliPrinter.cliPrintLn("2. Edit description")
        cliPrinter.cliPrintLn("3. States management")
        cliPrinter.cliPrintLn("0. Back to project")

        val input = cliReader.getValidUserInput(
            isValidInput = { it in listOf("0", "1", "2", "3") },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )

        when (input) {
            "1" -> editProjectTitle()
            "2" -> editProjectDescription()
            "3" -> statesManagement()
            "0" -> return currentProject
        }
        return currentProject
    }

    private fun editProjectTitle() {
        val newTitle = cliReader.getValidUserInput(
            message = "Enter new project title: ",
            invalidInputMessage = "Title cannot be empty",
            isValidInput = { it.isNotBlank() }
        )
        projectUseCases.editProjectTitle(currentProject.id, newTitle)
        currentProject = currentProject.copy(title = newTitle)
        cliPrinter.cliPrintLn("Project title updated.")
        cliPrinter.printHeader("Edit Project: ${currentProject.title}") // Show updated header
    }

    private fun editProjectDescription() {
        val newDescription = cliReader.getValidUserInput(
            message = "Enter new project description: ",
            invalidInputMessage = "Description cannot be empty",
            isValidInput = { it.isNotBlank() }
        )
        projectUseCases.editProjectDescription(currentProject.id, newDescription)
        currentProject = currentProject.copy(description = newDescription)
        cliPrinter.cliPrintLn("Project description updated.")
    }

    private fun statesManagement() {
        TODO("Not yet implemented")
    }
}