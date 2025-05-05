package data.dto

import java.util.UUID

data class TaskStateDto(
    val id: UUID,
    val title: String,
    val description: String,
    val projectId: UUID,
    val isDeleted: Boolean
)