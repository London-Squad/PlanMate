package data.cacheData

import data.fileIO.createFileIfNotExist
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.repositories.CacheDataRepository
import java.io.File
import java.util.*

class CacheDataSource(
    private val activeUserFile: File
) : CacheDataRepository {
    private var loggedInUser: User? = null

    init {
        activeUserFile.createFileIfNotExist("")
        loggedInUser = loadUserFromLocalFile()
    }

    override fun getLoggedInUser(): User {
        if (loggedInUser == null) throw NoLoggedInUserIsSavedInCacheException()
        return loggedInUser!!
    }

    override fun setLoggedInUser(user: User) {
//        if (user.type == User.Type.MATE)
            activeUserFile.writeText("${user.id},${user.userName},${user.type}")
        loggedInUser = user
    }

    override fun clearLoggedInUserFromCache() {
        activeUserFile.writeText("")
        loggedInUser = null
    }

    private fun loadUserFromLocalFile(): User? {
        val text = activeUserFile.readText().trim()
        if (text.isEmpty()) return null
        return text.split(",")
            .run { User(this[0].toUUID(), this[1], getUserTypeFromString(this[2])) }
    }

    private fun String.toUUID(): UUID {
        return UUID.fromString(this)
    }

    private fun getUserTypeFromString(type:String): User.Type {
        return when(type.lowercase()) {
            "admin" -> User.Type.ADMIN
            else -> User.Type.MATE
        }
    }
}