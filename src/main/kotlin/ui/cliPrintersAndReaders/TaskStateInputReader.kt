package ui.cliPrintersAndReaders

import logic.validation.TaskStateInputValidator

class TaskStateInputReader(
    private val cliReader: CLIReader,
    private val taskStateInputValidator: TaskStateInputValidator,
) {
    fun getValidTaskStateTitle(): String {
        return cliReader.getValidUserInput(
            message = "Enter title: ",
            invalidInputMessage = "Title cannot be empty, and max Title length is ${TaskStateInputValidator.MAX_TASK_STATE_TITLE_LENGTH}",
            isValidInput = taskStateInputValidator::isValidTaskStateTitle
        )
    }

    fun getValidTaskStateDescription(): String {
        return cliReader.getValidUserInput(
            message = "Enter description: ",
            invalidInputMessage = "Max description length is ${TaskStateInputValidator.MAX_TASK_STATE_DESCRIPTION_LENGTH}",
            isValidInput = taskStateInputValidator::isValidTaskStateDescription
        )
    }
}