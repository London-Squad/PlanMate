package data.repositories.dtoMappers

import data.dto.TaskStateDto
import logic.entities.TaskState
import java.util.UUID


fun TaskStateDto.toTaskState(): TaskState {
    return TaskState(
        id = this.id,
        title = this.title,
        description = this.description
    )
}

fun TaskState.toTaskStateDto(projectId: UUID, isDeleted: Boolean = false): TaskStateDto {
    return TaskStateDto(
        id = this.id,
        title = this.title,
        description = this.description,
        projectId = projectId,
        isDeleted = isDeleted
    )
}