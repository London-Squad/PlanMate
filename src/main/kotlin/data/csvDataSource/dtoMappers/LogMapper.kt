package data.csvDataSource.dtoMappers

import data.dto.LogDto
import logic.entities.*
import logic.exceptions.RetrievingDataFailureException

fun LogDto.toLog(user: User, planEntity: PlanEntity): Log {
    val action = when (this.action.lowercase()) {
        "create" -> EntityCreationLog(entity = planEntity)
        "delete" -> EntityDeletionLog(entity = planEntity)
        "edit" -> EntityEditionLog(
            entity = planEntity,
            property = this.planEntityProperty,
            oldValue = this.oldValue,
            newValue = this.newValue
        )

        else -> throw RetrievingDataFailureException("Unknown action type: ${this.action}")
    }

    return Log(
        id = this.id,
        user = user,
        time = this.time,
        logEntry = action
    )
}

fun Log.toLogDto(): LogDto {
    val action = when (this.logEntry) {
        is EntityCreationLog -> "create"
        is EntityDeletionLog -> "delete"
        is EntityEditionLog -> "edit"
    }

    return LogDto(
        id = this.id,
        userId = this.user.id,
        time = this.time,
        action = action,
        planEntityId = this.logEntry.entity.id,
        planEntityProperty = if (this.logEntry is EntityEditionLog) this.logEntry.property else "Nan",
        oldValue = if (this.logEntry is EntityEditionLog) this.logEntry.oldValue else "Nan",
        newValue = if (this.logEntry is EntityEditionLog) this.logEntry.newValue else "Nan"
    )
}