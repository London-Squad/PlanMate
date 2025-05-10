package data.repositories.dtoMappers

import data.dto.ProjectDto
import logic.entities.Project
import logic.entities.Task
import logic.entities.TaskState

fun ProjectDto.toProject(tasks: List<Task>, tasksStates: List<TaskState>): Project {
    return Project(
        id = this.id,
        title = this.title,
        description = this.description,
    )
}

fun Project.toProjectDto(isDeleted: Boolean = false): ProjectDto {
    return ProjectDto(
        id = this.id,
        title = this.title,
        description = this.description,
        isDeleted = isDeleted
    )
}