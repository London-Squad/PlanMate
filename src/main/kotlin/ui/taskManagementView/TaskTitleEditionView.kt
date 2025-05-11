package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.UUID

class TaskTitleEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val baseView: BaseView

) {
    fun editTitle(taskId: UUID) {
        val newTitle = taskInputReader.getValidTaskTitle()
        baseView.tryCall {
            manageTaskUseCase.editTaskTitle(taskId, newTitle)
        }
    }
}