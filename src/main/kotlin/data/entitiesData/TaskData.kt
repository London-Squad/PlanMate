package data.entitiesData

import java.util.UUID

data class TaskData(
    val id: UUID,
    val title: String,
    val description: String,
    val stateId: UUID,
    val projectId: UUID,
    val isDeleted: Boolean
)