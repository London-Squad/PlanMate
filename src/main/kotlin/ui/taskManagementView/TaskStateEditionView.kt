package ui.taskManagementView

import logic.entities.TaskState
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskStateEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler

) {

    fun editState(task: Task, projectTasksStates: List<TaskState>) {
        if (projectTasksStates.isEmpty()) {
            printLn("no states available")
            return
        }
        printProjectState(projectTasksStates)
        val newStateIndex = cliReader.getValidUserNumberInRange(min = 1, max = projectTasksStates.size).toInt() - 1

        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskState(task.id, projectTasksStates[newStateIndex])
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