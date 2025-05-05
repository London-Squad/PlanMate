package ui.taskManagementView

import logic.entities.State
import logic.entities.Task
import logic.exceptions.NotFoundException
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

    fun editState(task: Task, projectStates: List<State>) {
        if (projectStates.isEmpty()) {
            printLn("no states available")
            return
        }
        printProjectState(projectStates)
        val newStateIndex = cliReader.getValidUserNumberInRange(min = 1, max = projectStates.size).toInt() - 1

        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskState(task.id, projectStates[newStateIndex])
        }
    }

    private fun printProjectState(states: List<State>) {
        states.forEachIndexed { index, state ->
            printLn("${index + 1}. ${state.title}")
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}