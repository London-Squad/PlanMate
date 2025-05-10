package logic.entities

import java.util.UUID

data class Project(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String
)