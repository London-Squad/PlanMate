package data.entitiesData

import java.util.UUID

data class TaskStateData(
    val id: UUID,
    val title: String,
    val description: String,
    val projectId: UUID,
    val isDeleted: Boolean
)