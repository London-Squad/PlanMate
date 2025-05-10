package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.ProjectDto
import data.repositories.dataSourceInterfaces.ProjectsDataSource
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import org.bson.Document
import java.util.*

class MongoDBProjectsDataSource(
    private val projectsCollection: MongoCollection<Document>, private val mongoParse: MongoDBParse
) : ProjectsDataSource {

    override fun getAllProjects(includeDeleted: Boolean): List<ProjectDto> {
        try {
            return projectsCollection.find().map { doc ->
                mongoParse.documentToProjectDto(doc)
            }.toList().filter { if (includeDeleted) true else !it.isDeleted }
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve projects: ${e.message}")
        }
    }

    override fun addNewProject(project: ProjectDto) {
        try {
            val doc = mongoParse.projectDtoToDocument(project)
            projectsCollection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add project: ${e.message}")
        }
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        try {
            val result = projectsCollection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()), Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
            )
            if (result.matchedCount.toInt() == 0) {
                throw ProjectNotFoundException("Project with ID $projectId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit project title: ${e.message}")
        }
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        try {
            val result = projectsCollection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
                Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
            )
            if (result.matchedCount.toInt() == 0) {
                throw ProjectNotFoundException("Project with ID $projectId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit project description: ${e.message}")
        }
    }

    override fun deleteProject(projectId: UUID) {
        try {
            val result = projectsCollection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
                Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            )
            if (result.matchedCount.toInt() == 0) {
                throw ProjectNotFoundException("Project with ID $projectId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete project: ${e.message}")
        }
    }
}