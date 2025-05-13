package data.repositories.dtoMappers

import data.dto.LogDto
import logic.entities.*
import logic.exceptions.RetrievingDataFailureException
import java.time.LocalDateTime
import java.util.*

fun LogDto.toLog(): Log {
    val action = when (action.lowercase()) {
        "create" -> EntityCreationLog(entityId = UUID.fromString(planEntityId))
        "delete" -> EntityDeletionLog(entityId = UUID.fromString(planEntityId))
        "edit" -> EntityEditionLog(
            entityId = UUID.fromString(planEntityId),
            property = planEntityProperty,
            oldValue = oldValue,
            newValue = newValue
        )

        else -> throw RetrievingDataFailureException("Unknown action type: $action")
    }

    return Log(
        id = UUID.fromString(id),
        userId = UUID.fromString(userId),
        time = LocalDateTime.parse(time),
        loggedAction = action
    )
}

fun Log.toLogDto(): LogDto {
    val action = when (loggedAction) {
        is EntityCreationLog -> "create"
        is EntityDeletionLog -> "delete"
        is EntityEditionLog -> "edit"
    }

    return LogDto(
        id = id.toString(),
        userId = userId.toString(),
        time = time.toString(),
        action = action,
        planEntityId = loggedAction.getEntityId().toString(),
        planEntityProperty = if (loggedAction is EntityEditionLog) loggedAction.property else "Nan",
        oldValue = if (loggedAction is EntityEditionLog) loggedAction.oldValue else "Nan",
        newValue = if (loggedAction is EntityEditionLog) loggedAction.newValue else "Nan"
    )
}

private fun LoggedAction.getEntityId(): UUID {
    return when(this) {
        is EntityCreationLog -> entityId
        is EntityDeletionLog -> entityId
        is EntityEditionLog -> entityId
    }
}