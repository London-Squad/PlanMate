package ui.taskManagementView

import logic.useCases.ManageTaskUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.*

class TaskDescriptionEditionView(
    private val taskInputReader: TaskInputReader,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val cliPrinter: CLIPrinter,
) : BaseView(cliPrinter) {

    fun editDescription(taskId: UUID) {
        val newDescription = taskInputReader.getValidTaskDescription()
        makeRequest(
            request = { manageTaskUseCase.editTaskDescription(taskId, newDescription) },
            onSuccess = { cliPrinter.cliPrintLn("Task description updated successfully") },
            onLoadingMessage = "Updating task description..."
        )
    }
}