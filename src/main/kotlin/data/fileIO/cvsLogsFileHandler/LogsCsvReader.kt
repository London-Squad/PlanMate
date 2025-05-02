package data.fileIO.cvsLogsFileHandler

import java.io.File

class LogsCsvReader(
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

    fun readRows(): List<String> {
        return logsFile
            .readLines()
            .drop(1)
            .filter { it.split(",").size == EXPECTED_COLUMNS }
    }

    companion object {
        private const val EXPECTED_COLUMNS = 9
    }
}
