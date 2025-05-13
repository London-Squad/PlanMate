package data.repositories.dtoMappers

import data.dto.LogDto
import logic.entities.*
import logic.exceptions.RetrievingDataFailureException
import java.util.*

fun LogDto.toLog(): Log {
    val action = when (action.lowercase()) {
        "create" -> EntityCreationLog(entityId = planEntityId)
        "delete" -> EntityDeletionLog(entityId = planEntityId)
        "edit" -> EntityEditionLog(
            entityId = planEntityId,
            property = planEntityProperty,
            oldValue = oldValue,
            newValue = newValue
        )

        else -> throw RetrievingDataFailureException("Unknown action type: $action")
    }

    return Log(
        id = id,
        userId = userId,
        time = time,
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
        id = id,
        userId = userId,
        time = time,
        action = action,
        planEntityId = loggedAction.getEntityId(),
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