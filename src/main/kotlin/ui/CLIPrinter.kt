package ui

class CLIPrinter {

    fun cliPrint(message: String = "") {
        print(message)
    }

    fun cliPrintLn(message: String = "") {
        cliPrint(message + "\n")
    }

    private fun String.duplicate(numberOfDuplication: Int) =
        List(numberOfDuplication) { this }.joinToString(separator = "")

    fun getThickHorizontal() =
        THICK_HORIZONTAL_ELEMENT.duplicate(VIEW_WIDTH)

    fun getThinHorizontal() =
        THIN_HORIZONTAL_ELEMENT.duplicate(VIEW_WIDTH)

    private fun String.center(): String {
        val emptySpaceOnEachSide = (VIEW_WIDTH - this.length) / 2
        if (emptySpaceOnEachSide <= 0) return this
        return " ".duplicate(emptySpaceOnEachSide) + this + " ".duplicate(emptySpaceOnEachSide)
    }

    fun printHeader(headerText: String) {
        cliPrintLn("\n" + getThickHorizontal())
        cliPrintLn(headerText.center())
        cliPrintLn(getThickHorizontal() + "\n")
    }

    fun printTextWithinWidth(text: String, indent: Int = 0) {
        text.chunked(VIEW_WIDTH - indent).forEach {
            cliPrintLn(
                " ".duplicate(indent) + it
            )
        }
    }

    companion object {
        const val VIEW_WIDTH = 70
        const val THICK_HORIZONTAL_ELEMENT = "="
        const val THIN_HORIZONTAL_ELEMENT = "-"
    }
}