package logic.entities

import java.util.UUID

sealed class User(
    open val id: UUID = UUID.randomUUID(),
    open val userName: String,
)

data class Mate(
    override val id: UUID = UUID.randomUUID(),
    override val userName: String,
    val admin: Admin
): User(id = id, userName = userName)

data class Admin(
    override val id: UUID = UUID.randomUUID(),
    override val userName: String,
    val projects: List<Project> = listOf()
): User(id = id, userName = userName)
