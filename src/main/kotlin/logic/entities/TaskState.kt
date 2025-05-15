package logic.entities

import java.util.UUID

data class TaskState(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val projectId: UUID
)