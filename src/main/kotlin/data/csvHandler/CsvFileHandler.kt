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
}