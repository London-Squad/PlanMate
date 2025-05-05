package data.csvDataSource.fileIO

import logic.entities.User
import java.io.File
import java.util.*

private typealias FileLines = Sequence<String>

class UserFileHelper {
    companion object {
        fun readUserOrNull(file: File, userName: String, password: String): User? {
            return file.useLines { lines -> lines.searchForUser(userName, password) }
        }

        fun lineToUser(line: String): User {
            return line.trim().split(",").run {
                User(UUID.fromString(this[0]), this[1], User.Type.MATE)
            }
        }

        fun isUserExistInFile(file: File, userName: String, password: String): Boolean {
            return readUserOrNull(file, userName, password) != null
        }

        fun isUserNameExistInFile(file: File, userName: String): Boolean {
            return file.useLines { lines -> lines.searchForUserName(userName) } != null
        }

        fun writeUser(file: File, id: UUID, userName: String, password: String): Boolean {
            file.appendText("$id,$userName,$password\n")
            return true
        }

        fun clearAndWriteNewData(file: File, lines: List<String>) {
            file.writeText(lines.joinToString("\n"))
        }

        private fun FileLines.searchForUserName(userName: String): User? {
            val user = filter { it.contains(userName, ignoreCase = true) }.firstOrNull()
            if (user == null) return null
            return User(userName = userName, type = User.Type.MATE)
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
    }
}