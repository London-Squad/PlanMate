package ui.projectView

import logic.entities.Project
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.taskManagementView.TaskManagementView

class ProjectTasksView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val taskManagementView: TaskManagementView
) {

    private lateinit var currentProject: Project

    fun manageTasks(project: Project) {
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

        cliPrinter.cliPrintLn("1. Add new task")
        cliPrinter.cliPrintLn("2. Select task")
        cliPrinter.cliPrintLn("0. Back to project")

        val input = cliReader.getValidUserInput(
            isValidInput = { it in listOf("0", "1", "2") },
            message = "Choose an option: ",
            invalidInputMessage = "Invalid option, try again ..."
        )

        when (input) {
            "1" -> addNewTask()
            "2" -> selectTask()
            "0" -> return
        }
    }

    private fun addNewTask() {
        cliPrinter.cliPrintLn("ADD NEW TASK IS NOT IMPLEMENTED YET")
//        TODO("Not yet implemented")
    }

    private fun selectTask() {
        val tasks = currentProject.tasks
        if (tasks.isEmpty()) {
            cliPrinter.cliPrintLn("No tasks available to select.")
            return
        }

        cliPrinter.cliPrintLn("Available tasks:")
        tasks.forEachIndexed { index, task ->
            cliPrinter.cliPrintLn("${index + 1}. ${task.title} (State: ${task.state.title})")
        }

        val taskIndex = cliReader.getValidUserInput(
            message = "Select a task (1-${tasks.size}): ",
            invalidInputMessage = "Invalid task selection",
            isValidInput = { it.toIntOrNull() != null && it.toInt() in 1..tasks.size }
        ).toInt() - 1

        val selectedTask = tasks[taskIndex]
        taskManagementView.start(selectedTask, currentProject)
    }
}