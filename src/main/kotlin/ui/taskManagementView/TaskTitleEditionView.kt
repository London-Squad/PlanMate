package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.UUID

class TaskTitleEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val viewExceptionHandler: ViewExceptionHandler

) {
    fun editTitle(taskId: UUID) {
        val newTitle = taskInputReader.getValidTaskTitle()
        viewExceptionHandler.tryCall {
            manageTaskUseCase.editTaskTitle(taskId, newTitle)
        }
    }
}