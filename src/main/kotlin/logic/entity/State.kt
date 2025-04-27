package logic.entity

import java.util.*

data class State (
    val id: UUID,
    val title: String,
    val description: String,
    val tasks: List<Task>,
    val isDeleted: Boolean = false,
)