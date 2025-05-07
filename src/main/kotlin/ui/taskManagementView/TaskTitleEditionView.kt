package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.TaskInputReader

class TaskTitleEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler

) {
    fun editTitle(task: Task) {
        val newTitle = taskInputReader.getValidTaskTitle()
        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskTitle(task.id, newTitle)
        }
    }
}