package data.dbEntities

import java.util.UUID

data class DBUser(
    val id: UUID,
    val userName: String,
    val password: String,
    val type: String,
    val isDeleted: Boolean
)