package logic.entity

sealed class Action

data class Create(val entity: PlanEntity) : Action()

data class Delete(val entity: PlanEntity) : Action()

data class Edit(
    val entity: PlanEntity,
    val property: String,
    val oldValue: String,
    val newValue: String
) : Action()