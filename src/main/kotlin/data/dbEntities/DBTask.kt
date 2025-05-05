package data.dbEntities

import java.util.UUID

data class DBTask(
    val id: UUID,
    val title: String,
    val description: String,
    val stateID: UUID,
    val projectID: UUID,
    val isDeleted: Boolean
)