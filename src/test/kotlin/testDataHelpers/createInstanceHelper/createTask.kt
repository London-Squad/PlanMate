package testDataHelpers.createInstanceHelper

import logic.entities.Task
import logic.entities.State
import java.time.LocalDateTime
import java.util.UUID

fun createTask(
    id: UUID = UUID.randomUUID(),
    title: String = "Test Task ${LocalDateTime.now().nano}",
    description: String = "Default task description",
    state: State = State.NoState
): Task = Task(
    id = id,
    title = title,
    description = description,
    state = state
)