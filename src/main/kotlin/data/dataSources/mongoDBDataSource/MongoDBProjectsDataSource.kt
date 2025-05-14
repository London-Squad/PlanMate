package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBQueryHandler
import data.repositories.dtoMappers.toProject
import data.repositories.dtoMappers.toProjectDto
import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.util.*

class MongoDBProjectsDataSource(
    private val projectQueryHandler: MongoDBQueryHandler,
    private val mongoParser: MongoDBParser
) : ProjectsRepository {

    override suspend fun getAllProjects(includeDeleted: Boolean): List<Project> {
        val filters = if (!includeDeleted) Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        else Filters.empty()

        return projectQueryHandler.fetchManyFromCollection(filters)
            .map { doc -> mongoParser.documentToProjectDto(doc).toProject() }
    }

    override suspend fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, projectId.toString()),
            if (!includeDeleted) Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
            else Filters.empty()
        )
        return projectQueryHandler.fetchOneFromCollection(filters).let(mongoParser::documentToProjectDto).toProject()
    }

    override suspend fun addNewProject(project: Project) {
        project
            .toProjectDto()
            .let(mongoParser::projectDtoToDocument)
            .also { projectQueryHandler.insertToCollection(it) }
    }

    override suspend fun editProjectTitle(projectId: UUID, newTitle: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, projectId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        projectQueryHandler.updateCollection(MongoDBParser.TITLE_FIELD, newTitle, filters)
    }

    override suspend fun getProjectTitleById(projectId: UUID): String {
        return getProjectById(projectId,true).title
    }

    override suspend fun editProjectDescription(projectId: UUID, newDescription: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, projectId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        projectQueryHandler.updateCollection(MongoDBParser.TITLE_FIELD, newDescription, filters)
    }

    override suspend fun deleteProject(projectId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, projectId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        projectQueryHandler.softDeleteFromCollection(filters)
    }
}