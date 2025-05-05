package data.csvDataSource.fileIO.cvsLogsFileHandler

import java.io.File

class LogsCsvWriter(
    private val logsFile: File
) {

    init {
        val directory = logsFile.parentFile
        if (!directory.exists()) {
            directory.mkdir()
        }

        if (!logsFile.exists()) {
            logsFile.createNewFile()
            logsFile.writeText("id,userId,actionType,entityId,entityType,time,property,oldValue,newValue\n")
        }
    }

    fun appendLine(csvRow: String) {
        logsFile.appendText(csvRow + "\n")
    }
}
