package data.csvDataSource.dtoMappers

import data.dto.LogDto
import logic.entities.*

fun LogDto.toLog(user: User, planEntity: PlanEntity): Log {
    val action = when (this.action.lowercase()) {
        "create" -> Create(entity = planEntity)
        "delete" -> Delete(entity = planEntity)
        "edit" -> Edit(
            entity = planEntity,
            property = this.planEntityProperty,
            oldValue = this.oldValue,
            newValue = this.newValue
        )

        else -> throw IllegalArgumentException("Unknown action type: ${this.action}")
    }

    return Log(
        id = this.id,
        user = user,
        time = this.time,
        action = action
    )
}

fun Log.toLogDto(): LogDto {
    val action = when (this.action) {
        is Create -> "create"
        is Delete -> "delete"
        is Edit -> "edit"
    }

    return LogDto(
        id = this.id,
        userId = this.user.id,
        time = this.time,
        action = action,
        planEntityId = this.action.entity.id,
        planEntityProperty = if (this.action is Edit) this.action.property else "Nan",
        oldValue = if (this.action is Edit) this.action.oldValue else "Nan",
        newValue = if (this.action is Edit) this.action.newValue else "Nan"
    )
}