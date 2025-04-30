package testDataHelpers.createInstanceHelper

import logic.entities.State
import logic.entities.Task
import java.time.LocalDateTime
import java.util.*

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