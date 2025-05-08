package data.repositories.dtoMappers

import data.dto.TaskDto
import logic.entities.Task
import logic.entities.TaskState
import java.util.UUID

fun TaskDto.toTask(taskState: TaskState): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        taskState = taskState
    )
}

fun Task.toTaskDto(projectId: UUID, isDeleted: Boolean = false): TaskDto {
    return TaskDto(
        id = this.id,
        title = this.title,
        description = this.description,
        stateId = this.taskState.id,
        projectId = projectId,
        isDeleted = isDeleted
    )
}