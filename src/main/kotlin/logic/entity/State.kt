package logic.entity

import java.util.UUID

data class State(
    override val id: UUID = UUID.randomUUID(),
    override val title: String,
    override val description: String,
): PlanEntity