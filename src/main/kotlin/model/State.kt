package model

import java.util.*

data class State (
    val id: UUID,
    val title: String,
    val description: String,
    val deleted: Boolean = false
)