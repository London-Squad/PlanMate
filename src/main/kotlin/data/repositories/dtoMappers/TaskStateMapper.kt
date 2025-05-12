package data.repositories.dtoMappers

import data.dto.TaskStateDto
import logic.entities.TaskState
import java.util.UUID


fun TaskStateDto.toTaskState(): TaskState {
    return TaskState(
        id = id,
        title = title,
        description = description,
        projectId = projectId
    )
}

fun TaskState.toTaskStateDto(projectId: UUID, isDeleted: Boolean = false): TaskStateDto {
    return TaskStateDto(
        id = id,
        title = title,
        description = description,
        projectId = projectId,
        isDeleted = isDeleted
    )
}