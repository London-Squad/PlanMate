package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.ProjectsDataSource
import data.entitiesData.ProjectData
import java.util.UUID

class CsvProjectsDataSource(
    private val projectsCsvFileHandler: CsvFileHandler,
    private val parser: Parser,
) : ProjectsDataSource {

    override fun getAllProjects(): List<ProjectData> {
        return projectsCsvFileHandler.readRecords()
            .map(parser::recordToProjectData)
    }

    override fun addNewProject(project: ProjectData) {
        projectsCsvFileHandler.appendRecord(
            parser.projectDataToRecord(project)
        )
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val newProjectData = parser.recordToTaskData(it)
                if (newProjectData.id == projectId) {
                    return@map parser.taskDataToRecord(newProjectData.copy(title = newTitle))
                }
                it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsCsvFileHandler.readRecords()
            .map {
                val newProjectData = parser.recordToTaskData(it)
                if (newProjectData.id == projectId) {
                    return@map parser.taskDataToRecord(newProjectData.copy(description = newDescription))
                }
                it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }

    override fun deleteProject(projectId: UUID) {
        projectsCsvFileHandler.readRecords()
            .map {
                val newProjectData = parser.recordToTaskData(it)
                if (newProjectData.id == projectId) {
                    return@map parser.taskDataToRecord(newProjectData.copy(isDeleted = true))
                }
                it
            }
            .also(projectsCsvFileHandler::rewriteRecords)
    }
}