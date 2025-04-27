package logic.entity
import java.time.LocalDateTime
import java.util.UUID

data class Log(
    val id: UUID = UUID.randomUUID(),
    val user: User,
    val time: LocalDateTime,
    val action: Action,
)