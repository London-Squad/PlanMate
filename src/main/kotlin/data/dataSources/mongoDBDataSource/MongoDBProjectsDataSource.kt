package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dtoMappers.toProject
import data.repositories.dtoMappers.toProjectDto
import logic.entities.Project
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.repositories.ProjectsRepository
import org.bson.Document
import java.util.*

class MongoDBProjectsDataSource(
    private val projectsCollection: MongoCollection<Document>, private val mongoParse: MongoDBParse
) : ProjectsRepository {

    override fun getAllProjects(includeDeleted: Boolean): List<Project> {
        try {
            val filter = if (includeDeleted) Filters.exists(MongoDBParse.ID_FIELD)
            else Filters.and(
                Filters.exists(MongoDBParse.ID_FIELD), Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )
            return projectsCollection.find(filter).map { doc -> mongoParse.documentToProjectDto(doc).toProject() }.toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve projects: ${e.message}")
        }

    }

    override fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        val filter = if (includeDeleted) {
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString())
        } else {
            Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )
        }

        val document = projectsCollection.find(filter).first() ?: throw RetrievingDataFailureException(
            "Project with ID $projectId not found"
        )

        return mongoParse.documentToProjectDto(document).toProject()
    }

    override fun addNewProject(project: Project) {
        try {
            val projectDto = project.toProjectDto(isDeleted = false)
            val document = mongoParse.projectDtoToDocument(projectDto)
            projectsCollection.insertOne(document)
        }catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add project: ${e.message}")
        }
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            val updateResult = projectsCollection.updateOne(
                filter, Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        )

        if (updateResult.matchedCount == 0L) {
            throw RetrievingDataFailureException("Project with ID $projectId not found")
        }
            } catch (e: MongoException) {
        throw StoringDataFailureException("Failed to edit project title: ${e.message}")
    }
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        try{
        val filter = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()), Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )

        val updateResult = projectsCollection.updateOne(
            filter, Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
        )

        if (updateResult.matchedCount == 0L) {
            throw RetrievingDataFailureException("Project with ID $projectId not found")
        }
    } catch (e: MongoException) {
        throw StoringDataFailureException("Failed to edit project description: ${e.message}")
    }
    }

    override fun deleteProject(projectId: UUID) {
        try{
        val filter = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, projectId.toString()), Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )

        val updateResult = projectsCollection.updateOne(
            filter, Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
        )

        if (updateResult.matchedCount == 0L) {
            throw RetrievingDataFailureException("Project with ID $projectId not found")
        }
    } catch (e: MongoException) {
        throw StoringDataFailureException("Failed to delete project: ${e.message}")
    }

    }
}