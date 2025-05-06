package logic.entities

import java.util.UUID

data class Task(
    override val id: UUID = UUID.randomUUID(),
    override val title: String,
    override val description: String,
    val taskState: TaskState = TaskState.NoTaskState
): PlanEntity