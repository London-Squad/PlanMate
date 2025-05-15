package logic.entities

import java.time.LocalDateTime
import java.util.UUID

data class Log(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val time: LocalDateTime = LocalDateTime.now(),
    val loggedAction: LoggedAction,
    val entityType: EntityType
) {
    enum class EntityType {
        TASK_STATE, TASK, PROJECT, USER
    }
}