package data.repositories.dtoMappers

import data.dto.TaskDto
import logic.entities.Task
import java.util.*

fun TaskDto.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        taskStateId = stateId
    )
}

fun Task.toTaskDto(projectId: UUID, isDeleted: Boolean = false): TaskDto {
    return TaskDto(
        id = id,
        title = title,
        description = description,
        stateId = taskStateId,
        projectId = projectId,
        isDeleted = isDeleted
    )
}