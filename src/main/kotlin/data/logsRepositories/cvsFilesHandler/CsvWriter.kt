package data.logsRepositories.cvsFilesHandler


import java.io.File

class CsvWriter {
    fun appendLine(path: String, line: String) {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.appendText(line + System.lineSeparator())
    }
}
