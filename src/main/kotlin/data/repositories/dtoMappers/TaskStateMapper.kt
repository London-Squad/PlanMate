package data.repositories.dtoMappers

import data.dto.TaskStateDto
import logic.entities.TaskState
import java.util.UUID


fun TaskStateDto.toTaskState(): TaskState {
    return TaskState(
        id = UUID.fromString(id),
        title = title,
        description = description,
        projectId = UUID.fromString(projectId)
    )
}

fun TaskState.toTaskStateDto(projectId: UUID, isDeleted: Boolean = false): TaskStateDto {
    return TaskStateDto(
        id = id.toString(),
        title = title,
        description = description,
        projectId = projectId.toString(),
        isDeleted = isDeleted
    )
}