package model

import java.util.*

data class Log (
    val user: User,
    val time: Date,
    val description: String,
    val task: Task?,
    val project: Project
)