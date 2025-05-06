package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskDescriptionEditionView(
    private val cliReader: CLIReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    fun editDescription(task: Task) {
        val newDescription = cliReader.getValidDescription()
        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskDescription(task.id, newDescription)
        }
    }
}