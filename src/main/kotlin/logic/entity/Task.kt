package logic.entity

import java.util.UUID

data class Task (
    val id: UUID,
    val title: String,
    val description: String,
    val isDeleted: Boolean = false,
)