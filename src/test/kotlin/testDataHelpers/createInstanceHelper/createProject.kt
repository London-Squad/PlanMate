package testDataHelpers.createInstanceHelper

import logic.entities.Project
import logic.entities.Task
import logic.entities.State
import java.time.LocalDateTime
import java.util.UUID

fun createProject(
    id: UUID = UUID.randomUUID(),
    title: String = "Test Project ${LocalDateTime.now()}",
    description: String = "Default project description",
    tasks: List<Task> = listOf(createTask()),
    states: List<State> = listOf(createState())
): Project = Project(
    id = id,
    title = title,
    description = description,
    tasks = tasks,
    states = states
)

