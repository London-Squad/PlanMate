package ui.projectView

import logic.entities.Project
import logic.entities.Task
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.taskManagementView.TaskManagementView
import java.util.UUID

class ProjectTasksView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val projectUseCases: ProjectUseCases,
    private val taskManagementView: TaskManagementView
) {

    private lateinit var currentProject: Project

    fun manageTasks(project: Project): Project {
        currentProject = project
        cliPrinter.printHeader("Manage Tasks: ${currentProject.title}")

        val tasks = currentProject.tasks
        if (tasks.isEmpty()) {
            cliPrinter.cliPrintLn("No tasks available in this project.")
        } else {
            cliPrinter.cliPrintLn("Current tasks:")
            tasks.forEachIndexed { index, task ->
                cliPrinter.cliPrintLn("${index + 1}. ${task.title} (State: ${task.state.title})")
                cliPrinter.cliPrintLn("   Description: ${task.description}")
            }
        }
        cliPrinter.cliPrintLn("")

        if (tasks.isEmpty()) {
            cliPrinter.cliPrintLn("Type 'add' to add a new task")
        } else {
            cliPrinter.cliPrintLn("Enter a task number to select (1-${tasks.size}), 'add' to add a new task")
        }
        cliPrinter.cliPrintLn("Type '0' to return to project")

        val validInputs = if (tasks.isEmpty()) {
            listOf("add", "0")
        } else {
            (0..tasks.size).map { it.toString() } + "add"
        }

        val input = cliReader.getValidUserInput(
            isValidInput = { it in validInputs },
            message = "Enter your choice: ",
            invalidInputMessage = "Invalid option, try again ..."
        )

        when (input) {
            "add" -> addNewTask()
            "0" -> return currentProject
            else -> {
                val taskIndex = input.toInt() - 1
                val selectedTask = tasks[taskIndex]
                taskManagementView.start(selectedTask, currentProject)
            }
        }
        return currentProject
    }

    private fun addNewTask() {
        if (currentProject.states.isEmpty()) {
            cliPrinter.cliPrintLn("No states available for this project. Cannot create task.")
            return
        }

        val defaultState = currentProject.states.first()
        val title = cliReader.getValidTitle()
        val description = cliReader.getValidDescription()

        val newTask = Task(
            id = UUID.randomUUID(),
            title = title,
            description = description,
            state = defaultState
        )

        currentProject = currentProject.copy(tasks = currentProject.tasks + newTask)
        projectUseCases.updateProject(currentProject)
        projectUseCases.logTaskCreation(newTask)
        cliPrinter.cliPrintLn("Task created. You can now edit it.")
        taskManagementView.start(newTask, currentProject)

    }
}