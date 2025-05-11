package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.UUID

class TaskDescriptionEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val baseView: BaseView
) {

    fun editDescription(taskId: UUID) {
        val newDescription = taskInputReader.getValidTaskDescription()
        baseView.tryCall {
            manageTaskUseCase.editTaskDescription(taskId, newDescription)
        }
    }
}