package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.TaskInputReader

class TaskDescriptionEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler
) {

    fun editDescription(task: Task) {
        val newDescription = taskInputReader.getValidTaskDescription()
        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskDescription(task.id, newDescription)
        }
    }
}