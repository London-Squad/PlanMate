package logic.entities

sealed class LoggedAction(open val entity: PlanEntity)

data class EntityCreationLog(override val entity: PlanEntity) : LoggedAction(entity)

data class EntityDeletionLog(override val entity: PlanEntity) : LoggedAction(entity)

data class EntityEditionLog(
    override val entity: PlanEntity,
    val property: String,
    val oldValue: String,
    val newValue: String
) : LoggedAction(entity)