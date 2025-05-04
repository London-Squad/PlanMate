package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.cliPrintersAndReaders.CLIReader

class TaskDescriptionEditionView(
    private val manageTaskUseCase: ManageTaskUseCase,
    private val cliReader: CLIReader
) {

    fun editDescription(task: Task) {
        val newDescription = cliReader.getValidDescription()
        manageTaskUseCase.editTaskDescription(task.id, newDescription)
    }
}