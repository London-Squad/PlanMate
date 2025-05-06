package data.csvDataSource.fileIO
import data.exceptions.DataRetrievalFailureException
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
        } catch (e: Exception) {
            throw DataRetrievalFailureException("Failed to read CSV file at ${file.path}")
        }
    }

    fun appendRecord(record: List<String>) {
        try {
            file.appendText("${encodeRecord(record)}\n")
        } catch (e: Exception) {
            throw DataRetrievalFailureException("Failed to append record to CSV file at ${file.path}")
        }
    }

    fun rewriteRecords(records: List<List<String>>) {
        try {
            file.writeText("")
            if (records.isEmpty()) return
            records.forEach(::appendRecord)
        } catch (e: Exception) {
            throw DataRetrievalFailureException("Failed to rewrite CSV file at ${file.path}")
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