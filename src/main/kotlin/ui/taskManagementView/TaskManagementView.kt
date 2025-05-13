package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageStateUseCase
import logic.useCases.ManageTaskUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.logsView.LogsView
import java.util.*

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
) : RequestHandler(cliPrinter) {

    private lateinit var task: Task
    private lateinit var taskStateTitle: String
    private lateinit var projectId: UUID

    fun start(taskId: UUID, projectId: UUID) {

        this.projectId = projectId
        makeRequest(
            request = { fetchTaskInfo(taskId) },
            onSuccess = {
                printTask()
                printOptions()
                selectNextUI()
            },
            onLoadingMessage = "Fetching task info..."
        )


    }

    private suspend fun fetchTaskInfo(taskId: UUID) {
        task = manageTaskUseCase.getTaskByID(taskId)
        taskStateTitle = manageStateUseCase.getTaskStatesById(task.taskStateId).title
    }

    private fun printTask() {
        cliPrinter.printHeader("Task Title: ${task.title}")
        cliPrinter.cliPrintLn("Details:")
        cliPrinter.cliPrintLn("  - Description: ${task.description}")
        cliPrinter.cliPrintLn("  - State: $taskStateTitle")
        cliPrinter.cliPrintLn("")
    }

    private fun printOptions() {
        cliPrinter.cliPrintLn("Options:")
        cliPrinter.cliPrintLn("1. Edit Title")
        cliPrinter.cliPrintLn("2. Edit description")
        cliPrinter.cliPrintLn("3. Edit state")
        cliPrinter.cliPrintLn("4. Delete task")
        cliPrinter.cliPrintLn("5. View task logs")
        cliPrinter.cliPrintLn("0. Back")
    }

    private fun selectNextUI() {
        when (cliReader.getValidInputNumberInRange(MAX_OPTION_NUMBER)) {
            1 -> taskTitleEditionView.editTitle(task.id)
            2 -> taskDescriptionEditionView.editDescription(task.id)
            3 -> taskStateEditionView.editState(task.id, projectId)
            4 -> {
                taskDeletionView.deleteTask(task.id)
                return
            }

            5 -> logsView.printLogsByEntityId(task.id)
        }
        start(task.id, projectId)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 5
    }
}