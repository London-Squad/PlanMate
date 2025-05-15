package data.repositories.dtoMappers

import data.dto.ProjectDto
import logic.entities.Project
import java.util.UUID

fun ProjectDto.toProject(): Project {
    return Project(
        id = UUID.fromString(id),
        title = title,
        description = description
    )
}

fun Project.toProjectDto(isDeleted: Boolean = false): ProjectDto {
    return ProjectDto(
        id = id.toString(),
        title = title,
        description = description,
        isDeleted = isDeleted
    )
}