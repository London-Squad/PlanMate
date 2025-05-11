package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.*

class TaskTitleEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    cliPrinter: CLIPrinter,
) : BaseView(cliPrinter) {
    fun editTitle(taskId: UUID) {

        val newTitle = taskInputReader.getValidTaskTitle()

        makeRequest({
            manageTaskUseCase.editTaskTitle(taskId, newTitle)
        })
    }
}