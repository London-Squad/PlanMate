package logic.entities

import java.util.UUID

data class State(
    override val id: UUID = UUID.randomUUID(),
    override val title: String,
    override val description: String,
): PlanEntity {
    companion object {
        val NoState = State(UUID.randomUUID(), "No State", "this is the default state for tasks with no state")
    }
}