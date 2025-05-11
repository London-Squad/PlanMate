package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.ProjectDto
import data.repositories.dataSourceInterfaces.ProjectsDataSource
import java.util.*

class CsvProjectsDataSource(
    private val projectsCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser,
) : ProjectsDataSource {

    override suspend fun getAllProjects(includeDeleted: Boolean): List<ProjectDto> {
        return projectsCsvFileHandler.readRecords()
            .map(csvParser::recordToProjectDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override suspend fun addNewProject(project: ProjectDto) {
        projectsCsvFileHandler.appendRecord(
            csvParser.projectDtoToRecord(project)
        )
    }

    override suspend fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = csvParser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    csvParser.projectDtoToRecord(projectData.copy(title = newTitle))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override suspend fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = csvParser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    csvParser.projectDtoToRecord(projectData.copy(description = newDescription))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override suspend fun deleteProject(projectId: UUID) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = csvParser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    csvParser.projectDtoToRecord(projectData.copy(isDeleted = true))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }
}