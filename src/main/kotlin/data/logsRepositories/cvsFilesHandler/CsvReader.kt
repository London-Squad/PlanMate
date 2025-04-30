package data.logsRepositories.cvsFilesHandler

import java.io.File
import java.io.IOException

class CsvReader {

    fun readLines(filePath: String): List<String> {
        val file = File(filePath)

        if (!file.exists()) {
            println("Warning: The file $filePath does not exist.")
            return emptyList()
        }

        if (!file.canRead()) {
            println("Error: Cannot read the file $filePath. Check permissions.")
            return emptyList()
        }

        return try {
            val lines = file.readLines()

            lines.filter { it.isNotBlank() && it.split(",").size == EXPECTED_COLUMNS }
        } catch (e: IOException) {
            println("Error: Failed to read the file $filePath. Exception: ${e.message}")
            emptyList()
        }
    }

    companion object {
        private const val EXPECTED_COLUMNS = 6
    }
}
