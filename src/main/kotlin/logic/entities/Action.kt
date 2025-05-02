package logic.entities

sealed class Action(open val entity: PlanEntity)

data class Create(override val entity: PlanEntity) : Action(entity)

data class Delete(override val entity: PlanEntity) : Action(entity)

data class Edit(
    override val entity: PlanEntity,
    val property: String,
    val oldValue: String,
    val newValue: String
) : Action(entity)