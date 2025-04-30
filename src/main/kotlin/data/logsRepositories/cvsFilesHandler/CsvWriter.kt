package data.logsRepositories.cvsFilesHandler

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class CsvWriter {

    fun appendLine(path: String, line: String) {
        val file = File(path)

        try {

            file.parentFile?.mkdirs()

            val writer = BufferedWriter(FileWriter(file, true))
            writer.use {
                it.write(line)
                it.newLine()
            }
        } catch (e: IOException) {
            println("Error: Failed to write to the file $path. Exception: ${e.message}")
        }
    }
}
