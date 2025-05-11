package ui.cliPrintersAndReaders.cliTable

import ui.cliPrintersAndReaders.CLIPrinter

class CLITablePrinter(private val cliPrinter: CLIPrinter) {

    operator fun invoke(
        headers: List<String>, data: List<List<String>>, columnsWidth: List<Int?>
    ) {
        if (!areInputsValid(headers, data, columnsWidth)) {
            cliPrinter.cliPrintLn("invalid table inputs")
            return
        }

        val finalColumnWidths = calculateColumnWidths(headers, data, columnsWidth)
        printSeparator(finalColumnWidths)
        printHeader(headers, finalColumnWidths)
        printSeparator(finalColumnWidths)
        printData(data, finalColumnWidths)
        printBottomBorder(finalColumnWidths)
    }

    private fun areInputsValid(headers: List<String>, data: List<List<String>>, columnsWidth: List<Int?>): Boolean {
        if (data.isEmpty()) return false
        if (columnsWidth.size != headers.size) return false
        if (data.any { it.size != headers.size }) return false
        return true
    }

    private fun calculateColumnWidths(
        headers: List<String>, data: List<List<String>>, columnsWidth: List<Int?>
    ): List<Int> {
        val finalWidths = mutableListOf<Int>()
        if (data.isEmpty()) {
            headers.forEachIndexed { index, header ->
                val specifiedWidth = columnsWidth.getOrNull(index)
                finalWidths.add(specifiedWidth ?: (header.length + 2))
            }
            return finalWidths
        }
        for (colIndex in headers.indices) {
            val specifiedWidth = columnsWidth.getOrNull(colIndex)
            if (specifiedWidth != null) {
                finalWidths.add(specifiedWidth)
            } else {
                val headerLength = headers[colIndex].length
                val maxDataLength = data.maxOfOrNull { row ->
                    row.getOrNull(colIndex)?.length ?: 0
                } ?: 0
                val maxLength = maxOf(headerLength, maxDataLength) + 2
                finalWidths.add(maxLength)
            }
        }
        return finalWidths
    }

    private fun printHeader(headers: List<String>, columnWidths: List<Int>) {
        cliPrinter.cliPrint("║")
        headers.forEachIndexed { index, header ->
            val width = columnWidths[index]
            val paddedHeader = header.take(width).padEnd(width)
            cliPrinter.cliPrint(" $paddedHeader ║")
        }
        cliPrinter.cliPrintLn("")
    }

    private fun printSeparator(columnWidths: List<Int>) {
        cliPrinter.cliPrint("╠")
        columnWidths.forEachIndexed { index, width ->
            cliPrinter.cliPrint("═".repeat(width + 2))
            if (index != columnWidths.lastIndex) cliPrinter.cliPrint("╬")
        }
        cliPrinter.cliPrintLn("╣")
    }

    private fun printBottomBorder(columnWidths: List<Int>) {
        cliPrinter.cliPrint("╚")
        columnWidths.forEachIndexed { index, width ->
            cliPrinter.cliPrint("═".repeat(width + 2))
            if (index != columnWidths.lastIndex) cliPrinter.cliPrint("╩") else cliPrinter.cliPrint("╝\n")
        }
    }

    private fun printData(data: List<List<String>>, columnWidths: List<Int>) {
        data.forEach { row ->
            cliPrinter.cliPrint("║")
            row.forEachIndexed { colIndex, cell ->
                val width = columnWidths[colIndex]
                val trimmedCell = cell.take(width).padEnd(width)
                cliPrinter.cliPrint(" $trimmedCell ║")
            }
            cliPrinter.cliPrintLn("")
        }
    }

}