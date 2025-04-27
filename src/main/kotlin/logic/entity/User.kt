package logic.entity

import java.util.*

data class User (
    val id: UUID,
    val userName: String,
    val hashedPassword: String,
    val type: UserType,
    val deleted: Boolean = false
)

enum class UserType {
    ADMIN,
    MATE
}