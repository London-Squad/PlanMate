package logic.entities
import java.time.LocalDateTime
import java.util.UUID

data class Log(
    val id: UUID = UUID.randomUUID(),
    val time: LocalDateTime = LocalDateTime.now(),
    val userId: UUID,
    val planEntityId: UUID,
    val message: String
)