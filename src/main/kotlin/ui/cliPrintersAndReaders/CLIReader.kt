package ui.cliPrintersAndReaders

import logic.validation.UserInputValidator


class CLIReader(
    private val cliPrinter: CLIPrinter,
    private val userInputValidator: UserInputValidator
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

    fun getValidTitle(): String {
        return getValidUserInput(
            message = "Enter title: ",
            invalidInputMessage = UserInputValidator.INVALID_TITLE_MESSAGE,
            isValidInput = userInputValidator::isValidTitle
        )
    }

    fun getValidDescription(): String {
        return getValidUserInput(
            message = "Enter description: ",
            invalidInputMessage = UserInputValidator.INVALID_DESCRIPTION_MESSAGE,
            isValidInput = userInputValidator::isValidDescription
        )
    }

/**
 * @return "y" or "n" string
 **/
fun getUserConfirmation(): Boolean {
    return getUserInput("Do you confirm? (y/n): ") == "y"
    }

    fun getValidUserNumberInRange(max: Int, min: Int = 0): Int {
        return getValidUserInput(
            message = "Your choice: ",
            invalidInputMessage = "Invalid input",
            isValidInput = { it.trim().toIntOrNull() in min..max }
        ).trim().toInt()
    }
}
