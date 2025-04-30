package ui.cliPrintersAndReaders

class CLIPrinter {

    fun cliPrint(message: String = "") {
        print(message)
    }

    fun cliPrintLn(message: String = "") {
        cliPrint(message + "\n")
    }

    fun printPleaseLoginMessage() {
        cliPrintLn(pleaseLoginMessage)
    }

    fun printHeader(headerText: String) {
        cliPrintLn("\n" + getThickHorizontal())
        cliPrintLn(headerText.center())
        cliPrintLn(getThickHorizontal() + "\n")
    }

    fun getThickHorizontal() =
        THICK_HORIZONTAL_ELEMENT.duplicate(VIEW_WIDTH)

    fun getThinHorizontal() =
        THIN_HORIZONTAL_ELEMENT.duplicate(VIEW_WIDTH)

    fun printTextWithinWidth(text: String, indent: Int = 0) {
        text.chunked(VIEW_WIDTH - indent).forEach {
            cliPrintLn(
                " ".duplicate(indent) + it
            )
        }
    }

    private fun String.duplicate(numberOfDuplication: Int) =
        List(numberOfDuplication) { this }.joinToString(separator = "")

    private fun String.center(): String {
        val numberOfEmptySpaceOnEachSide = (VIEW_WIDTH - this.length) / 2
        if (numberOfEmptySpaceOnEachSide <= 0) return this
        return " ".duplicate(numberOfEmptySpaceOnEachSide) + this + " ".duplicate(numberOfEmptySpaceOnEachSide)
    }

    companion object {
        const val VIEW_WIDTH = 70
        const val THICK_HORIZONTAL_ELEMENT = "="
        const val THIN_HORIZONTAL_ELEMENT = "-"
        const val pleaseLoginMessage = "please login to continue ..."
    }

}
