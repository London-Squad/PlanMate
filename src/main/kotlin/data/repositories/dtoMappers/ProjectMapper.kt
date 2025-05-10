package data.repositories.dtoMappers

import data.dto.ProjectDto
import logic.entities.Project

fun ProjectDto.toProject(): Project {
    return Project(
        id = id,
        title = title,
        description = description
    )
}

fun Project.toProjectDto(isDeleted: Boolean = false): ProjectDto {
    return ProjectDto(
        id = id,
        title = title,
        description = description,
        isDeleted = isDeleted
    )
}