package logic.entities

sealed class LoggedAction(open val entity: PlanEntity)

data class LogCreate(override val entity: PlanEntity) : LoggedAction(entity)

data class LogDelete(override val entity: PlanEntity) : LoggedAction(entity)

data class LogEdit(
    override val entity: PlanEntity,
    val property: String,
    val oldValue: String,
    val newValue: String
) : LoggedAction(entity)