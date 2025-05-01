package data.fileIO

import logic.entities.User
import java.io.File
import java.util.*

private typealias FileLines = Sequence<String>

fun File.createFileIfNotExist(headers: String?): Boolean {
    if (exists()) return false
    createNewFile()
    headers?.let { writeText(headers) }
    return true
}

fun File.readUserOrNull(userName: String, password: String): User? {
    return useLines { lines -> lines.searchForUser(userName, password) }
}

fun lineToUser(line: String): User {
    return line.trim().split(",").run {
        User(UUID.fromString(this[0]), this[1], User.Type.MATE)
    }
}

fun File.isUserExistInFile(userName: String, password: String): Boolean {
    return readUserOrNull(userName, password) != null
}

fun File.writeUser(id: UUID, userName: String, password: String): Boolean {
    appendText("$id,$userName,$password\n")
    return true
}

fun File.clearAndWriteNewData(lines: List<String>) {
    writeText(lines.joinToString("\n"))
}

private fun FileLines.searchForUser(
    userName: String,
    password: String
): User? {
    val user = filter { it.contains("$userName,$password", ignoreCase = true) }
        .firstOrNull()

    if (user == null) return null
    return User(userName = userName, type = User.Type.MATE)
}