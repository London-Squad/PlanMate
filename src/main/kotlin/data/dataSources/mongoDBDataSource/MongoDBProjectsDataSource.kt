package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.ProjectMongoDto
import data.repositories.dtoMappers.toProject
import data.repositories.dtoMappers.toProjectMongoDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.repositories.ProjectsRepository
import java.util.*

class MongoDBProjectsDataSource(
    private val projectsCollection: MongoCollection<ProjectMongoDto>
) : ProjectsRepository {

    override suspend fun getAllProjects(includeDeleted: Boolean): List<Project> {
        return try {
            val filter = if (!includeDeleted) Filters.eq(ProjectMongoDto::isDeleted.name, false)
            else Filters.empty()

            projectsCollection.find<ProjectMongoDto>(filter)
                .map(ProjectMongoDto::toProject)
                .toList()
        } catch (e: MongoException) {
            println(e.stackTrace.toString())
            throw RetrievingDataFailureException("Failed to retrieve projects: ${e.message}")
        }

    }

    override suspend fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        return try {
            val filter = Filters.and(
                Filters.eq("_id", projectId.toString()),
                if (!includeDeleted) Filters.eq(ProjectMongoDto::isDeleted.name, false)
                else Filters.empty()
            )

            projectsCollection.find(filter)
                .firstOrNull()
                ?.toProject()
                ?: throw ProjectNotFoundException("Project with ID $projectId not found")

        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve project: ${e.message}")
        }
    }

    override suspend fun addNewProject(project: Project) {
        try {
            projectsCollection.insertOne(project.toProjectMongoDto())
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add project: ${e.message}")
        }
    }

    override suspend fun editProjectTitle(projectId: UUID, newTitle: String) {
        try {
            val filter = Filters.and(
                Filters.eq("_id", projectId.toString()),
                Filters.eq(ProjectMongoDto::isDeleted.name, false)
            )

            projectsCollection.updateOne(
                filter, Updates.set(ProjectMongoDto::title.name, newTitle)
            ).apply {
                if (matchedCount == 0L) {
                    throw ProjectNotFoundException("Project with ID $projectId not found")
                }
            }

        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit project title: ${e.message}")
        }
    }

    override suspend fun editProjectDescription(projectId: UUID, newDescription: String) {
        try {
            val filter = Filters.and(
                Filters.eq("_id", projectId.toString()),
                Filters.eq(ProjectMongoDto::isDeleted.name, false)
            )

            projectsCollection.updateOne(
                filter, Updates.set(ProjectMongoDto::description.name, newDescription)
            ).apply {
                if (matchedCount == 0L) {
                    throw ProjectNotFoundException("Project with ID $projectId not found")
                }
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit project description: ${e.message}")
        }
    }

    override suspend fun deleteProject(projectId: UUID) {
        try {
            val filter = Filters.and(
                Filters.eq("_id", projectId.toString()),
                Filters.eq(ProjectMongoDto::isDeleted.name, false)
            )

            projectsCollection.updateOne(
                filter, Updates.set(ProjectMongoDto::isDeleted.name, true)
            ).apply {
                if (matchedCount == 0L) {
                    throw ProjectNotFoundException("Project with ID $projectId not found")
                }
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete project: ${e.message}")
        }

    }
}