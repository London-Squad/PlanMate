package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.dtoMappers.toProject
import data.repositories.dtoMappers.toProjectDto
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.repositories.ProjectsRepository
import java.util.UUID

class CsvProjectsDataSource(
    private val projectsCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser,
) : ProjectsRepository {

    override suspend fun getAllProjects(includeDeleted: Boolean): List<Project> {
        return projectsCsvFileHandler.readRecords()
            .map(csvParser::recordToProjectDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map { it.toProject() }
    }

    override suspend fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        val projectDto = projectsCsvFileHandler.readRecords()
            .map { csvParser.recordToProjectDto(it) }
            .find { it.id == projectId.toString() }
            ?: throw ProjectNotFoundException("Project with ID $projectId not found")

        if (!includeDeleted && projectDto.isDeleted) {
            throw ProjectNotFoundException("Project with ID $projectId is deleted")
        }

        return projectDto.toProject()
    }

    override suspend fun addNewProject(project: Project) {
        val projectDto = project.toProjectDto(isDeleted = false)
        projectsCsvFileHandler.appendRecord(
            csvParser.projectDtoToRecord(projectDto)
        )
    }

    override suspend fun editProjectTitle(projectId: UUID, newTitle: String) {
        var projectFound = false

        projectsCsvFileHandler.readRecords().map {
            val projectDto = csvParser.recordToProjectDto(it)
            if (projectDto.id == projectId.toString() && !projectDto.isDeleted) {
                projectFound = true
                csvParser.projectDtoToRecord(projectDto.copy(title = newTitle))
            } else {
                it
            }
        }.also {
            if (!projectFound) throw ProjectNotFoundException("Project with ID $projectId not found")
            projectsCsvFileHandler.rewriteRecords(it)
        }
    }

    override suspend fun editProjectDescription(projectId: UUID, newDescription: String) {
        var projectFound = false

        projectsCsvFileHandler.readRecords().map {
            val projectDto = csvParser.recordToProjectDto(it)
            if (projectDto.id == projectId.toString() && !projectDto.isDeleted) {
                projectFound = true
                csvParser.projectDtoToRecord(projectDto.copy(description = newDescription))
            } else {
                it
            }
        }.also {
            if (!projectFound) throw ProjectNotFoundException("Project with ID $projectId not found")
            projectsCsvFileHandler.rewriteRecords(it)
        }
    }

    override suspend fun deleteProject(projectId: UUID) {
        var projectFound = false

        projectsCsvFileHandler.readRecords().map {
            val projectDto = csvParser.recordToProjectDto(it)
            if (projectDto.id == projectId.toString() && !projectDto.isDeleted) {
                projectFound = true
                csvParser.projectDtoToRecord(projectDto.copy(isDeleted = true))
            } else {
                it
            }
        }.also {
            if (!projectFound) throw ProjectNotFoundException("Project with ID $projectId not found")
            projectsCsvFileHandler.rewriteRecords(it)
        }
    }
}