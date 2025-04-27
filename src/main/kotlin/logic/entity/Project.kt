package logic.entity

import java.util.UUID

data class Project(
    override val id: UUID = UUID.randomUUID(),
    override val title: String,
    override val description: String,
    val tasks: List<Task>,
    val states: List<State>,
): PlanEntity