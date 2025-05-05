package data.DBEntities

import java.util.*

data class DBProject(
    val id: UUID,
    val title: String,
    val description: String,
    val isDeleted: Boolean
)
