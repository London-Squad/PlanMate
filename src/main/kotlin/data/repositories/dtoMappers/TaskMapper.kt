package data.repositories.dtoMappers

import data.dto.TaskDto
import logic.entities.Task
import java.util.*

fun TaskDto.toTask(): Task {
    return Task(
        id = UUID.fromString(id),
        title = title,
        description = description,
        taskStateId = UUID.fromString(stateId)
    )
}

fun Task.toTaskDto(projectId: UUID, isDeleted: Boolean = false): TaskDto {
    return TaskDto(
        id = id.toString(),
        title = title,
        description = description,
        stateId = taskStateId.toString(),
        projectId = projectId.toString(),
        isDeleted = isDeleted
    )
}