package ui.cliPrintersAndReaders

import logic.validation.ProjectInputValidator
import logic.validation.TaskInputValidator
import logic.validation.TaskStateInputValidator


class CLIReader(
    private val cliPrinter: CLIPrinter,
    private val projectInputValidator: ProjectInputValidator,
    private val taskInputValidator: TaskInputValidator,
    private val taskStateInputValidator: TaskStateInputValidator
) {

    fun getUserInput(message: String): String {
        cliPrinter.cliPrint(message)
        return readln()
    }

    fun getValidUserInput(
        isValidInput: (String) -> Boolean = { true },
        message: String = "your input: ",
        invalidInputMessage: String = "invalid input, try again ..."
    ): String {
        val userInput = getUserInput(message).trim()
        if (isValidInput(userInput)) return userInput
        cliPrinter.cliPrintLn(invalidInputMessage)
        return getValidUserInput(isValidInput, message, invalidInputMessage)
    }

    fun getValidProjectTitle(): String {
        return getValidUserInput(
            message = "Enter title: ",
            invalidInputMessage = ProjectInputValidator.INVALID_TITLE_MESSAGE,
            isValidInput = projectInputValidator::isValidProjectTitle
        )
    }

    fun getValidProjectDescription(): String {
        return getValidUserInput(
            message = "Enter description: ",
            invalidInputMessage = ProjectInputValidator.INVALID_DESCRIPTION_MESSAGE,
            isValidInput = projectInputValidator::isValidProjectDescription
        )
    }

    fun getValidTaskTitle(): String {
        return getValidUserInput(
            message = "Enter title: ",
            invalidInputMessage = TaskInputValidator.INVALID_TITLE_MESSAGE,
            isValidInput = taskInputValidator::isValidTaskTitle
        )
    }

    fun getValidTaskDescription(): String {
        return getValidUserInput(
            message = "Enter description: ",
            invalidInputMessage = TaskInputValidator.INVALID_DESCRIPTION_MESSAGE,
            isValidInput = taskInputValidator::isValidTaskDescription
        )
    }

    fun getValidTaskStateTitle(): String {
        return getValidUserInput(
            message = "Enter title: ",
            invalidInputMessage = TaskStateInputValidator.INVALID_TITLE_MESSAGE,
            isValidInput = taskStateInputValidator::isValidTaskStateTitle
        )
    }

    fun getValidTaskStateDescription(): String {
        return getValidUserInput(
            message = "Enter description: ",
            invalidInputMessage = TaskStateInputValidator.INVALID_DESCRIPTION_MESSAGE,
            isValidInput = taskStateInputValidator::isValidTaskStateDescription
        )
    }

    fun getUserConfirmation(): Boolean {
        return getValidUserInput(
            { it in listOf("y", "n") },
            "Do you confirm? (y/n): "
        ) == "y"
    }

    fun getValidInputNumberInRange(max: Int, min: Int = 0): Int {
        return getValidUserInput(
            message = "Your choice: ",
            invalidInputMessage = "Invalid input",
            isValidInput = { it.trim().toIntOrNull() in min..max }
        ).trim().toInt()
    }
}
