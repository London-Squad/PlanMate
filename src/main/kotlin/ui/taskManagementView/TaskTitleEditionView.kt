package ui.taskManagementView

import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.cliPrintersAndReaders.CLIReader

class TaskTitleEditionView(
    private val manageTaskUseCase: ManageTaskUseCase,
    private val cliReader: CLIReader,

    ) {

    fun editTitle(task: Task) {
        val newTitle = cliReader.getValidTitle()
        manageTaskUseCase.editTaskTitle(task.id, newTitle)
    }
}