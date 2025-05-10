package ui.taskManagementView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class TaskStateEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val manageStateUseCase: ManageStateUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    fun editState(taskId: UUID, projectId: UUID) {

        val projectTasksStates = manageStateUseCase.getTaskStatesByProjectId(projectId)
        if (projectTasksStates.isEmpty()) {
            printLn("no states available")
            return
        }

        printProjectState(projectTasksStates)
        val newStateIndex = cliReader.getValidInputNumberInRange(min = 1, max = projectTasksStates.size) - 1

        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskState(taskId, projectTasksStates[newStateIndex].id)
        }
    }

    private fun printProjectState(tasksStates: List<TaskState>) {
        tasksStates.forEachIndexed { index, state ->
            printLn("${index + 1}. ${state.title}")
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}