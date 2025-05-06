package ui.taskManagementView

import logic.entities.Task
import logic.exceptions.NotFoundException
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskTitleEditionView(
    private val cliReader: CLIReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler

) {

    fun editTitle(task: Task) {
        val newTitle = cliReader.getValidTitle()
        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskTitle(task.id, newTitle)
        }
    }
}