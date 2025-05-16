package data.repositories.dtoMappers

import data.dto.TaskCsvDto
import data.dto.TaskMongoDto
import logic.entities.Task
import java.util.*

fun TaskCsvDto.toTask(): Task {
    return Task(
        id = UUID.fromString(id),
        title = title,
        description = description,
        taskStateId = UUID.fromString(stateId)
    )
}

fun Task.toTaskDto(projectId: UUID, isDeleted: Boolean = false): TaskCsvDto {
    return TaskCsvDto(
        id = id.toString(),
        title = title,
        description = description,
        stateId = taskStateId.toString(),
        projectId = projectId.toString(),
        isDeleted = isDeleted
    )
}

fun TaskMongoDto.toTask(): Task {
    return Task(
        id = UUID.fromString(id),
        title = title,
        description = description,
        taskStateId = UUID.fromString(stateId)
    )
}

fun Task.toTaskMongoDto(projectId: UUID, isDeleted: Boolean = false): TaskMongoDto {
    return TaskMongoDto(
        id = id.toString(),
        title = title,
        description = description,
        stateId = taskStateId.toString(),
        projectId = projectId.toString(),
        isDeleted = isDeleted
    )
}