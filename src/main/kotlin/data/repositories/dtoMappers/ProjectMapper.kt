package data.repositories.dtoMappers

import data.dto.ProjectCsvDto
import data.dto.ProjectMongoDto
import logic.entities.Project
import java.util.UUID

fun ProjectCsvDto.toProject(): Project {
    return Project(
        id = UUID.fromString(id),
        title = title,
        description = description
    )
}

fun Project.toProjectDto(isDeleted: Boolean = false): ProjectCsvDto {
    return ProjectCsvDto(
        id = id.toString(),
        title = title,
        description = description,
        isDeleted = isDeleted
    )
}

fun ProjectMongoDto.toProject(): Project {
    return Project(
        id = UUID.fromString(id),
        title = title,
        description = description
    )
}

fun Project.toProjectMongoDto(isDeleted: Boolean = false): ProjectMongoDto {
    return ProjectMongoDto(
        id = id.toString(),
        title = title,
        description = description,
        isDeleted = isDeleted
    )
}