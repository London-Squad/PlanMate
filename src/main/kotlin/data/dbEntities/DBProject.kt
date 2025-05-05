package data.dbEntities

import java.util.UUID

data class DBProject(
    val id: UUID,
    val title: String,
    val description: String,
    val isDeleted: Boolean
)