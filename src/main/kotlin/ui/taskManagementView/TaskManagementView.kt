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

    private lateinit var currentTask: Task
    private lateinit var currentProject: Project
    fun start(taskID: UUID, project: Project) {

        try {

            currentTask = taskRepository.getTaskByID(taskID)
        } catch (e: NotFoundException) {
            cliPrinter.cliPrintLn(e.message ?: "task not found")
            return
        }

        currentProject = project
        printTask()
        printOptions()
        selectNextUI()
    }

    private fun printTask() {
        printLn("Task: ${currentTask.title}")
        printLn("Description: ${currentTask.description}")
        printLn("State: ${currentTask.state.title}")
    }

    private fun printOptions() {
        printLn("1. Edit Title")
        printLn("2. Edit description")
        printLn("3. Edit state")
        printLn("4. Delete task")
        printLn("5. View task logs")
        printLn("0. Back")
    }

    private fun selectNextUI() {
        when (getValidUserInput()) {
            "1" -> {
                taskTitleEditionView.editTitle(currentTask)
                start(currentTask.id, currentProject)
            }

            "2" -> {
                taskDescriptionEditionView.editDescription(currentTask)
                start(currentTask.id, currentProject)
            }

            "3" -> {
                taskStateEditionView.editState(currentTask, currentProject.states)
                start(currentTask.id, currentProject)
            }

            "4" -> {
                taskDeletionView.deleteTask(currentTask)
                return
            }

            "5" -> {
                logsView.printLogsByEntityId(currentTask.id)
                start(currentTask.id, currentProject)
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