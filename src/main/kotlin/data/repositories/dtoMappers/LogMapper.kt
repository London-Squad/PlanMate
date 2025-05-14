package data.repositories.dtoMappers

import data.dto.LogDto
import logic.entities.*
import logic.exceptions.RetrievingDataFailureException
import java.time.LocalDateTime
import java.util.*

fun LogDto.toLog(): Log {
    val action = when (action.lowercase()) {
        LogConstants.ACTION_CREATE -> EntityCreationLog(entityId = UUID.fromString(planEntityId))
        LogConstants.ACTION_DELETE -> EntityDeletionLog(entityId = UUID.fromString(planEntityId))
        LogConstants.ACTION_EDIT -> EntityEditionLog(
            entityId = UUID.fromString(planEntityId),
            property = planEntityProperty,
            oldValue = oldValue,
            newValue = newValue,
        )
        else -> throw RetrievingDataFailureException("Unknown action type: $action")
    }

    return Log(
        id = UUID.fromString(id),
        userId = UUID.fromString(userId),
        time = LocalDateTime.parse(time),
        loggedAction = action,
        entityType = Log.EntityType.valueOf(entityType.uppercase())
    )
}

fun Log.toLogDto(): LogDto {
    val action = when (loggedAction) {
        is EntityCreationLog -> LogConstants.ACTION_CREATE
        is EntityDeletionLog -> LogConstants.ACTION_DELETE
        is EntityEditionLog -> LogConstants.ACTION_EDIT
    }

    return LogDto(
        id = id.toString(),
        userId = userId.toString(),
        time = time.toString(),
        action = action,
        planEntityId = loggedAction.getEntityId().toString(),
        planEntityProperty = if (loggedAction is EntityEditionLog) loggedAction.property else LogConstants.DEFAULT_VALUE,
        oldValue = if (loggedAction is EntityEditionLog) loggedAction.oldValue else LogConstants.DEFAULT_VALUE,
        newValue = if (loggedAction is EntityEditionLog) loggedAction.newValue else LogConstants.DEFAULT_VALUE,
        entityType = entityType.name
    )
}

private fun LoggedAction.getEntityId(): UUID {
    return when(this) {
        is EntityCreationLog -> entityId
        is EntityDeletionLog -> entityId
        is EntityEditionLog -> entityId
    }
}
object LogConstants {
    const val ACTION_CREATE = "create"
    const val ACTION_DELETE = "delete"
    const val ACTION_EDIT = "edit"
    const val DEFAULT_VALUE = "Nan"
}