package data.csvDataSource.fileIO

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
        if (!file.exists()) return emptyList()
        return file.readLines().map(::decodeRecord)
    }

    fun appendRecord(record: List<String>) {
        file.appendText("${encodeRecord(record)}\n")
    }

    fun rewriteRecords(records: List<List<String>>) {
        if (records.isEmpty()) return
        file.writeText("")
        records.forEach(::appendRecord)
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