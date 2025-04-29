package ui.cLIPrintersAndReaders

class CLIReader(
    private val cliPrinter: CLIPrinter
) {

        fun getUserInput(message: String): String {
            cliPrinter.cliPrint(message)
            return readln()
        }

        fun getValidUserInput(
            isValidInput: (String)->Boolean = { true },
            message: String = "your input: ",
            invalidInputMessage: String = "invalid input, try again ..."
        ): String {
            val userInput = getUserInput(message).trim()
            if (isValidInput(userInput)) return userInput
            cliPrinter.cliPrintLn(invalidInputMessage)
            return getValidUserInput(isValidInput, message, invalidInputMessage)
        }

}