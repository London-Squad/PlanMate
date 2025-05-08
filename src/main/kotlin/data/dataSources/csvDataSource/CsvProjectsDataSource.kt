package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.ProjectsDataSource
import data.dto.ProjectDto
import java.util.UUID

class CsvProjectsDataSource(
    private val projectsCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser,
) : ProjectsDataSource {

    override fun getAllProjects(includeDeleted: Boolean): List<ProjectDto> {
        return projectsCsvFileHandler.readRecords()
            .map(csvParser::recordToProjectDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override fun addNewProject(project: ProjectDto) {
        projectsCsvFileHandler.appendRecord(
            csvParser.projectDtoToRecord(project)
        )
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = csvParser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    csvParser.projectDtoToRecord(projectData.copy(title = newTitle))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = csvParser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    csvParser.projectDtoToRecord(projectData.copy(description = newDescription))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override fun deleteProject(projectId: UUID) {
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