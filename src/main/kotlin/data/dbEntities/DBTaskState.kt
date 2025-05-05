package data.dbEntities

import java.util.UUID

data class DBTaskState(
    val id: UUID,
    val title: String,
    val description: String,
    val projectID: UUID,
    val isDeleted: Boolean
)