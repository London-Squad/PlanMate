package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.UUID

class TaskDescriptionEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    fun editDescription(taskId: UUID) {
        val newDescription = taskInputReader.getValidTaskDescription()
        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskDescription(taskId, newDescription)
        }
    }
}