package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageStateUseCase
import logic.useCases.ManageTaskUseCase
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
    private val manageTaskUseCase: ManageTaskUseCase,
    private val manageStateUseCase: ManageStateUseCase,
    private val logsView: LogsView,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    private lateinit var currentTask: Task
    private lateinit var taskProjectId: UUID

    fun start(taskId: UUID, projectId: UUID) {

        taskProjectId = projectId

        var taskState: String
        viewExceptionHandler.tryCall {
            currentTask = manageTaskUseCase.getTaskByID(taskId)
            taskState = manageStateUseCase.getTaskStatesById(currentTask.taskStateId).title
            printTask(taskState)
        }.also {
            if (!it) {
                cliPrinter.cliPrintLn("something went wrong")
                return
            }
        }

        printOptions()
        selectNextUI()
    }

    private fun printTask(taskState: String) {
        cliPrinter.printHeader("Task: ${currentTask.title}")
        printLn("Details:")
        printLn("  - Description: ${currentTask.description}")
        printLn("  - State: $taskState")
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
            1 -> taskTitleEditionView.editTitle(currentTask.id)
            2 -> taskDescriptionEditionView.editDescription(currentTask.id)
            3 -> taskStateEditionView.editState(currentTask.id, taskProjectId)
            4 -> {
                taskDeletionView.deleteTask(currentTask.id)
                return
            }

            5 -> logsView.printLogsByEntityId(currentTask.id)
            0 -> return
        }
        start(currentTask.id, taskProjectId)
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 5
    }
}