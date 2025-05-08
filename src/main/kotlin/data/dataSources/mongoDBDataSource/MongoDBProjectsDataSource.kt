package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.ProjectsDataSource
import data.dto.ProjectDto
import org.bson.Document
import java.util.UUID

class MongoDBProjectsDataSource(
    private val projectsCollection: MongoCollection<Document>,
    private val mongoParse: MongoDBParse
) : ProjectsDataSource {

    override fun getAllProjects(includeDeleted: Boolean): List<ProjectDto> {
        return projectsCollection.find().map { doc ->
            mongoParse.documentToProjectDto(doc)
        }.toList()
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override fun addNewProject(project: ProjectDto) {
        val doc = mongoParse.projectDtoToDocument(project)
        projectsCollection.insertOne(doc)
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsCollection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
            Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        )
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsCollection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
            Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
        )
    }

    override fun deleteProject(projectId: UUID) {
        projectsCollection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
            Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
        )
    }
}