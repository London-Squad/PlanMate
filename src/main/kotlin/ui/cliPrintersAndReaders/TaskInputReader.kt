package ui.cliPrintersAndReaders

import logic.validation.TaskInputValidator

class TaskInputReader(
    private val cliReader: CLIReader,
    private val taskInputValidator: TaskInputValidator,
) {
    fun getValidTaskTitle(): String {
        return cliReader.getValidUserInput(
            message = "Enter title: ",
            invalidInputMessage = "Title cannot be empty, and max Title length is ${TaskInputValidator.MAX_TASK_TITLE_LENGTH}",
            isValidInput = taskInputValidator::isValidTaskTitle
        )
    }

    fun getValidTaskDescription(): String {
        return cliReader.getValidUserInput(
            message = "Enter description: ",
            invalidInputMessage = "Max description length is ${TaskInputValidator.MAX_TASK_DESCRIPTION_LENGTH}",
            isValidInput = taskInputValidator::isValidTaskDescription
        )
    }
}