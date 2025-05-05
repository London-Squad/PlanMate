package data.csvStorage.fileIO

import java.io.File

fun File.createFileIfNotExist( headers: String?): Boolean {
    if (exists()) return false
    createNewFile()
    headers?.let { writeText(headers) }
    return true
}