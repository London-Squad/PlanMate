package data.csvDataSource.fileIO

import data.exceptions.IOException
import data.exceptions.csvDataException.CSVDataException
import data.exceptions.csvDataException.*
import java.io.File

class CsvFileHandler(
    private val file: File
) {

    init {
        val directory = file.parentFile
        if (!directory.exists()) {
            directory.mkdir()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    fun readRecords(): List<List<String>> {
        try {
            return file.readLines().map(::decodeRecord)
        } catch (e: IOException) {
            throw FileNotFound("Failed to read CSV file at ${file.path}")
        }
    }

    fun appendRecord(record: List<String>) {
        try {
            file.appendText("${encodeRecord(record)}\n")
        } catch (e: IOException) {
            throw WriteFailure("Failed to append record to CSV file at ${file.path}")
        }
    }

    fun rewriteRecords(records: List<List<String>>) {
        try {
            file.writeText("")
            if (records.isEmpty()) return
            records.forEach(::appendRecord)
        } catch (e: IOException) {
            throw WriteFailure("Failed to rewrite CSV file at ${file.path}")
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