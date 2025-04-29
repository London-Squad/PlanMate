package data.csv

interface CSVHandler {
    fun read(filename: String): List<List<String>>
    fun write(filename: String, data: List<List<String>>): Boolean
    fun isExists(filename: String): Boolean
}