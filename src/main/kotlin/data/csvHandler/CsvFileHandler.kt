package data.csvHandler

import java.io.File

class CsvFileHandler(private val file: File) {
    fun readLines(): List<String> = if (file.exists()) file.readLines() else emptyList()

    fun writeHeader(header: String) {
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
            file.writeText("$header\n")
        }
    }

    fun appendLine(line: String) = file.appendText("$line\n")

    fun rewriteLines(lines: List<String>) {
        if (lines.isEmpty()) {
            return
        }

        file.writeText("")
        lines.forEachIndexed { index, line ->
            if (index == 0) {
                file.writeText("$line\n")
            } else {
                appendLine(line)
            }
        }
    }

    companion object {
        private const val COMMA_ESCAPE = "__comma__"
        private const val LINE_BREAK_ESCAPE = "__line_break__"

        private fun encodeCell(cell: String): String {
            return cell.replace(",", COMMA_ESCAPE)
                .replace("\n", LINE_BREAK_ESCAPE)
        }

        private fun decodeCell(cell: String): String {
            return cell.replace(COMMA_ESCAPE, ",")
                .replace(LINE_BREAK_ESCAPE, "\n")
        }

        fun encodeRow(record: List<String>): String {
            return record.map { encodeCell(it) }.joinToString(separator = ",")
        }

        fun decodeRow(csvRow: String): List<String> {
            return csvRow.split(",").map { decodeCell(it) }
        }
    }
}