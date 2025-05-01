package ui.taskManagementView

import logic.entities.Project
import logic.entities.Task
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskManagementView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val taskTitleEditionView: TaskTitleEditionView,
    private val taskDescriptionEditionView: TaskDescriptionEditionView,
    private val taskStateEditionView: TaskStateEditionView,
    private val taskDeletionView: TaskDeletionView,
) {

    fun start(task: Task, project: Project) {
        printTask(task)
        printOptions()
        selectNextUI(task, project)
    }


    private fun printTask(task: Task) {
        printLn("Task: ${task.title}")
        printLn("Description: ${task.description}")
        printLn("State: ${task.state.title}")
    }

    private fun printOptions() {
        printLn("1. Edit Title")
        printLn("2. Edit description")
        printLn("3. Edit state")
        printLn("4. delete task")
        printLn("0. back")
    }

    private fun selectNextUI(task: Task, project: Project) {
        when (getValidUserInput()) {
            "1" -> taskTitleEditionView.editTitle(task)
            "2" -> taskDescriptionEditionView.editDescription(task)
            "3" -> taskStateEditionView.editState(task, project.states)
            "4" -> taskDeletionView.deleteTask(task)
            "0" -> return
        }
        start(task, project)
    }

    private fun getValidUserInput(): String {
        val userInput = cliReader.getUserInput("your option:")
        if (userInput in OPTIONS_LIST) return userInput
        else {
            printLn("Invalid option")
            return getValidUserInput()
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        val OPTIONS_LIST = listOf("0", "1", "2", "3", "4")
    }
}