package ui.CLIPrintersAndReaders

class CLIReader(
    private val cliPrinter: CLIPrinter
) {

    fun getUserInput(message: String): String {
        cliPrinter.cliPrint(message)
        return readln()
    }

}