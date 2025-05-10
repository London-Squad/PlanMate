package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.ProjectsDataSource
import data.dto.ProjectDto
import kotlinx.coroutines.flow.map
import org.bson.Document
import java.util.UUID
import kotlinx.coroutines.flow.toList

class MongoDBProjectsDataSource(
    private val projectsCollection: MongoCollection<Document>,
    private val mongoParse: MongoDBParse
) : ProjectsDataSource {

    override suspend fun getAllProjects(includeDeleted: Boolean): List<ProjectDto> {
        return projectsCollection.find()
            .map { doc -> mongoParse.documentToProjectDto(doc) }
            .toList()
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override suspend fun addNewProject(project: ProjectDto) {
        val doc = mongoParse.projectDtoToDocument(project)
        projectsCollection.insertOne(doc).let { }
    }

    override suspend fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsCollection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
            Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        ).let { }
    }

    override suspend fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsCollection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
            Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
        ).let { }
    }

    override suspend fun deleteProject(projectId: UUID) {
        projectsCollection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
            Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
        ).let { }
    }
}