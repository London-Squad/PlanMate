package model

import java.util.*

data class Project (
    val id: UUID,
    val title: String,
    val tasks: List<Task>,
    val states: List<State>,
    val deleted: Boolean = false
)