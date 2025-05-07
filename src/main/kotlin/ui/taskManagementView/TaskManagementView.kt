package ui.taskManagementView

import logic.entities.Project
import logic.entities.Task
import logic.repositories.TaskRepository
import ui.ViewExceptionHandler
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
    private val logsView: LogsView,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    private lateinit var currentTask: Task
    private lateinit var currentProject: Project

    fun start(taskID: UUID, project: Project) {
        viewExceptionHandler.tryCall {
            currentTask = taskRepository.getTaskByID(taskID)
        }

        currentProject = project
        printTask()
        printOptions()
        selectNextUI()
    }

    private fun printTask() {
        cliPrinter.printHeader("Task: ${currentTask.title}")
        printLn("Details:")
        printLn("  - Description: ${currentTask.description}")
        printLn("  - State: ${currentTask.taskState.title}")
        printLn("")
    }

    private fun printOptions() {
        printLn("Options:")
        printLn("1. Edit Title")
        printLn("2. Edit description")
        printLn("3. Edit state")
        printLn("4. Delete task")
        printLn("5. View task logs")
        printLn("0. Back")
    }

    private fun selectNextUI() {
        when (cliReader.getValidInputNumberInRange(MAX_OPTION_NUMBER)) {
            1 -> {
                taskTitleEditionView.editTitle(currentTask)
                start(currentTask.id, currentProject)
            }

            2 -> {
                taskDescriptionEditionView.editDescription(currentTask)
                start(currentTask.id, currentProject)
            }

            3 -> {
                taskStateEditionView.editState(currentTask, currentProject.tasksStates)
                start(currentTask.id, currentProject)
            }

            4 -> {
                taskDeletionView.deleteTask(currentTask)
                return
            }

            5 -> {
                logsView.printLogsByEntityId(currentTask.id)
                start(currentTask.id, currentProject)
            }

            0 -> return
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 5
    }
}