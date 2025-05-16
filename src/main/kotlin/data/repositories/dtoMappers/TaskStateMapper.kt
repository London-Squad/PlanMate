package data.repositories.dtoMappers

import data.dto.TaskStateCsvDto
import data.dto.TaskStateMongoDto
import logic.entities.TaskState
import java.util.UUID


fun TaskStateCsvDto.toTaskState(): TaskState {
    return TaskState(
        id = UUID.fromString(id),
        title = title,
        description = description,
        projectId = UUID.fromString(projectId)
    )
}

fun TaskState.toTaskStateDto(projectId: UUID, isDeleted: Boolean = false): TaskStateCsvDto {
    return TaskStateCsvDto(
        id = id.toString(),
        title = title,
        description = description,
        projectId = projectId.toString(),
        isDeleted = isDeleted
    )
}

fun TaskStateMongoDto.toTaskState(): TaskState {
    return TaskState(
        id = UUID.fromString(id),
        title = title,
        description = description,
        projectId = UUID.fromString(projectId)
    )
}

fun TaskState.toTaskStateMongoDto(projectId: UUID, isDeleted: Boolean = false): TaskStateMongoDto {
    return TaskStateMongoDto(
        id = id.toString(),
        title = title,
        description = description,
        projectId = projectId.toString(),
        isDeleted = isDeleted
    )
}