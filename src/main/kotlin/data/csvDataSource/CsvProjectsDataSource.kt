package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.ProjectsDataSource
import data.dto.ProjectDto
import java.util.UUID

class CsvProjectsDataSource(
    private val projectsCsvFileHandler: CsvFileHandler,
    private val parser: Parser,
) : ProjectsDataSource {

    override fun getAllProjects(): List<ProjectDto> {
        return projectsCsvFileHandler.readRecords()
            .map(parser::recordToProjectDto)
    }

    override fun addNewProject(project: ProjectDto) {
        projectsCsvFileHandler.appendRecord(
            parser.projectDtoToRecord(project)
        )
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = parser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    parser.projectDtoToRecord(projectData.copy(title = newTitle))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = parser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    parser.projectDtoToRecord(projectData.copy(description = newDescription))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override fun deleteProject(projectId: UUID) {
        projectsCsvFileHandler.readRecords()
            .map {
                val projectData = parser.recordToProjectDto(it)
                if (projectData.id == projectId)
                    parser.projectDtoToRecord(projectData.copy(isDeleted = true))
                else it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }
}