package data.dataSources.csvDataSource.fileIO

import logic.planeMateException.FileIOException
import java.io.File
import java.io.IOException

class CsvFileHandler(
    private val file: File
) {

    init {
        try {
            val directory = file.parentFile
            if (!directory.exists()) {
                directory.mkdir()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
        }catch (e: IOException) {
            throw FileIOException("Failed to initialize file: ${e.message}")
        }
    }

    fun readRecords(): List<List<String>> {
        if (!file.exists()) throw FileIOException("File does not exist: ${file.path}")
        return file.readLines().map(::decodeRecord)
    }

    fun appendRecord(record: List<String>) {
        try {
            file.appendText("${encodeRecord(record)}\n")
        }catch (e: IOException) {
            throw FileIOException("Failed to append record: ${e.message}")
        }
    }

    fun rewriteRecords(records: List<List<String>>) {
        try {
            file.writeText("")
            if (records.isEmpty()) return
            records.forEach(::appendRecord)
        }catch (e: IOException) {
            throw FileIOException("Failed to rewrite records: ${e.message}")
        }
    }

    private fun encodeCell(cell: String): String {
        return cell.replace(",", COMMA_ESCAPE)
            .replace("\n", LINE_BREAK_ESCAPE)
    }

    private fun decodeCell(cell: String): String {
        return cell.replace(COMMA_ESCAPE, ",")
            .replace(LINE_BREAK_ESCAPE, "\n")
    }

    private fun encodeRecord(record: List<String>): String {
        return record.map(::encodeCell).joinToString(separator = ",")
    }

    private fun decodeRecord(csvRow: String): List<String> {
        return csvRow.split(",").map(::decodeCell)
    }

    companion object {
        private const val COMMA_ESCAPE = "__comma__"
        private const val LINE_BREAK_ESCAPE = "__lineBreak__"
    }
}