package logic.entities

sealed class LogEntry(open val entity: PlanEntity)

data class LogCreate(override val entity: PlanEntity) : LogEntry(entity)

data class LogDelete(override val entity: PlanEntity) : LogEntry(entity)

data class LogEdit(
    override val entity: PlanEntity,
    val property: String,
    val oldValue: String,
    val newValue: String
) : LogEntry(entity)