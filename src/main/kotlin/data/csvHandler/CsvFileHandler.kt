package data.csvHandler

import java.io.File

class CsvFileHandler(private val file: File) {

    fun readRecord(): List<List<String>> {
        if (!file.exists()) return emptyList()
        return file.readLines().map(::decodeRecord)
    }

    fun writeHeader(header: String) {
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
            file.writeText("$header\n")
        }
    }

    fun appendRecord(record: List<String>) {
        file.appendText("${encodeRecord(record)}\n")
    }

    fun rewriteRecords(records: List<List<String>>) {
        if (records.isEmpty()) return

        file.writeText(file.readLines()[0] + "\n")
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
        return record.map { encodeCell(it) }.joinToString(separator = ",")
    }

    private fun decodeRecord(csvRow: String): List<String> {
        return csvRow.split(",").map { decodeCell(it) }
    }

    companion object {
        private const val COMMA_ESCAPE = "__comma__"
        private const val LINE_BREAK_ESCAPE = "__line_break__"
    }
}