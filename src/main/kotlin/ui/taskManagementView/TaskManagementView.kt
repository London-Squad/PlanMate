package ui.taskManagementView

import logic.entities.Project
import logic.entities.Task
import logic.exceptions.NotFoundException
import logic.repositories.TaskRepository
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.logsView.LogsView
import java.util.UUID

class TaskManagementView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val taskTitleEditionView: TaskTitleEditionView,
    private val taskDescriptionEditionView: TaskDescriptionEditionView,
    private val taskStateEditionView: TaskStateEditionView,
    private val taskDeletionView: TaskDeletionView,
    private val taskRepository: TaskRepository,
    private val logsView: LogsView
) {

    fun start(taskID: UUID, project: Project) {

        try {

            val task = taskRepository.getTaskByID(taskID)
            printTask(task)
            printOptions()
            selectNextUI(task, project)

        } catch (e: NotFoundException) {
            cliPrinter.cliPrintLn(e.message ?: "task not found")
        }
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
        printLn("4. Delete task")
        printLn("5. View task logs")
        printLn("0. Back")
    }

    private fun selectNextUI(task: Task, project: Project) {
        when (getValidUserInput()) {
            "1" -> {
                taskTitleEditionView.editTitle(task)
                start(task.id, project)
            }

            "2" -> {
                taskDescriptionEditionView.editDescription(task)
                start(task.id, project)
            }

            "3" -> {
                taskStateEditionView.editState(task, project.states)
                start(task.id, project)
            }

            "4" -> {
                taskDeletionView.deleteTask(task)
                return
            }

            "5" -> {
                logsView.printLogsByEntityId(task.id)
                start(task.id, project)
            }

            "0" -> return
        }
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
        val OPTIONS_LIST = listOf("0", "1", "2", "3", "4", "5")
    }
}