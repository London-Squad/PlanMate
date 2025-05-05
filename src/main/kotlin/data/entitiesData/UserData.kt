package data.entitiesData

import java.util.UUID

data class UserData(
    val id: UUID,
    val userName: String,
    val hashedPassword: String,
    val type: String,
    val isDeleted: Boolean
)
