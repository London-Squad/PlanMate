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
            logsFile.writeText("id,time,userId,planEntityId,message,projectId\n")
        }
    }

    fun readRows(): List<String> {
        return logsFile
            .readLines()
            .drop(1)
    }
}
