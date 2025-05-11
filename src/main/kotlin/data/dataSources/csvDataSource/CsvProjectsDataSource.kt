package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.dtoMappers.toProject
import data.repositories.dtoMappers.toProjectDto
import logic.entities.Project
import logic.exceptions.RetrievingDataFailureException
import logic.repositories.ProjectsRepository
import java.util.UUID

class CsvProjectsDataSource(
    private val projectsCsvFileHandler: CsvFileHandler,
    private val projectsRepository: ProjectsRepository,
    private val csvParser: CsvParser,
) : ProjectsRepository {

    override fun getAllProjects(includeDeleted: Boolean): List<Project> {
        return projectsCsvFileHandler.readRecords()
            .map { csvParser.recordToProjectDto(it) }
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map { it.toProject() }
    }

    override fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        val projectDto = projectsCsvFileHandler.readRecords()
            .map { csvParser.recordToProjectDto(it) }
            .find { it.id == projectId }
            ?: throw RetrievingDataFailureException("Project with ID $projectId not found")

        if (!includeDeleted && projectDto.isDeleted) {
            throw RetrievingDataFailureException("Project with ID $projectId is deleted")
        }

        return projectDto.toProject()
    }

    override fun addNewProject(project: Project) {
        val projectDto = project.toProjectDto(isDeleted = false)
        projectsCsvFileHandler.appendRecord(
            csvParser.projectDtoToRecord(projectDto)
        )
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        val records = projectsCsvFileHandler.readRecords()
        var projectFound = false

        val updatedRecords = records.map {
            val projectDto = csvParser.recordToProjectDto(it)
            if (projectDto.id == projectId && !projectDto.isDeleted) {
                projectFound = true
                csvParser.projectDtoToRecord(projectDto.copy(title = newTitle))
            } else {
                it
            }
        }

        if (!projectFound) {
            throw RetrievingDataFailureException("Project with ID $projectId not found")
        }

        projectsCsvFileHandler.rewriteRecords(updatedRecords)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        val records = projectsCsvFileHandler.readRecords()
        var projectFound = false

        val updatedRecords = records.map {
            val projectDto = csvParser.recordToProjectDto(it)
            if (projectDto.id == projectId && !projectDto.isDeleted) {
                projectFound = true
                csvParser.projectDtoToRecord(projectDto.copy(description = newDescription))
            } else {
                it
            }
        }

        if (!projectFound) {
            throw RetrievingDataFailureException("Project with ID $projectId not found")
        }

        projectsCsvFileHandler.rewriteRecords(updatedRecords)
    }

    override fun deleteProject(projectId: UUID) {
        val records = projectsCsvFileHandler.readRecords()
        var projectFound = false

        val updatedRecords = records.map {
            val projectDto = csvParser.recordToProjectDto(it)
            if (projectDto.id == projectId && !projectDto.isDeleted) {
                projectFound = true
                csvParser.projectDtoToRecord(projectDto.copy(isDeleted = true))
            } else {
                it
            }
        }

        if (!projectFound) {
            throw RetrievingDataFailureException("Project with ID $projectId not found")
        }

        projectsCsvFileHandler.rewriteRecords(updatedRecords)
    }

}