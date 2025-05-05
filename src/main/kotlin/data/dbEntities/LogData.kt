package data.dbEntities

import java.time.LocalDateTime
import java.util.UUID

data class LogData(
    val id: UUID,
    val userId: UUID,
    val time: LocalDateTime,
    val action: String,
    val planEntityId: UUID,
    val planEntityProperty: String,
    val oldValue: String,
    val newValue: String
)
