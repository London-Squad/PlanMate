package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.UUID

class TaskDescriptionEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    cliPrinter: CLIPrinter,
) : BaseView(cliPrinter) {

    fun editDescription(taskId: UUID) {
        val newDescription = taskInputReader.getValidTaskDescription()
        tryCall({
            manageTaskUseCase.editTaskDescription(taskId, newDescription)
        })
    }
}