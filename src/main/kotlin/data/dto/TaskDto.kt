package data.dto

import java.util.UUID

data class TaskDto(
    val id: UUID,
    val title: String,
    val description: String,
    val stateId: UUID,
    val projectId: UUID,
    val isDeleted: Boolean
)