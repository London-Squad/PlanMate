package logic.entities

import java.util.UUID

data class Project(
    override val id: UUID = UUID.randomUUID(),
    override val title: String,
    override val description: String,
    val tasks: List<Task>,
    val tasksStates: List<TaskState>,
): PlanEntity