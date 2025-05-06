package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.CsvParser
import data.dataSources.ProjectsDataSource
import data.dto.ProjectDto
import java.util.UUID

class CsvProjectsDataSource(
    private val projectsCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser,
) : ProjectsDataSource {

    override fun getAllProjects(): List<ProjectDto> {
        return projectsCsvFileHandler.readRecords()
            .map(csvParser::recordToProjectDto)
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