package ui.cliPrintersAndReaders

class CLIReader(
    private val cliPrinter: CLIPrinter,
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
