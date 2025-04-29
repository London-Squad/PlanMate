package data.logsRepositories.cvsFilesHandler

import java.io.File

class CsvReader {
    fun readLines(path: String): List<String> {
        val file = File(path)
        if (!file.exists()) return emptyList()
        return file.readLines()
    }
}
