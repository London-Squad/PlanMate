package logic.entity

import java.util.*

data class Project (
    val id: UUID,
    val title: String,
    val states: List<State>,
    val isDeleted: Boolean = false,
)