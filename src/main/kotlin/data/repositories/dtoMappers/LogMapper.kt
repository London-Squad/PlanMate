package data.repositories.dtoMappers

import data.dto.LogDto
import logic.entities.*
import logic.exceptions.RetrievingDataFailureException
import java.time.LocalDateTime
import java.util.*

private const val CREATION_ACTION_STRING = "create"
private const val DELETION_ACTION_STRING = "delete"
private const val EDITION_ACTION_STRING = "edit"
private const val DEFAULT_VALUE_STRING = "Nan"

fun LogDto.toLog(): Log {
    val action = when (action.lowercase()) {
        CREATION_ACTION_STRING -> EntityCreationLog(entityId = UUID.fromString(planEntityId))
        DELETION_ACTION_STRING -> EntityDeletionLog(entityId = UUID.fromString(planEntityId))
        EDITION_ACTION_STRING -> EntityEditionLog(
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
        is EntityCreationLog -> CREATION_ACTION_STRING
        is EntityDeletionLog -> DELETION_ACTION_STRING
        is EntityEditionLog -> EDITION_ACTION_STRING
    }

    return LogDto(
        id = id.toString(),
        userId = userId.toString(),
        time = time.toString(),
        action = action,
        planEntityId = loggedAction.getEntityId().toString(),
        planEntityProperty = if (loggedAction is EntityEditionLog) loggedAction.property else DEFAULT_VALUE_STRING,
        oldValue = if (loggedAction is EntityEditionLog) loggedAction.oldValue else DEFAULT_VALUE_STRING,
        newValue = if (loggedAction is EntityEditionLog) loggedAction.newValue else DEFAULT_VALUE_STRING,
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
