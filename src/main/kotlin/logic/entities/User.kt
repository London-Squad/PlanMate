package logic.entities

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val userName: String,
    val type: Type,
) {
    enum class Type {
        ADMIN,
        MATE
    }
}