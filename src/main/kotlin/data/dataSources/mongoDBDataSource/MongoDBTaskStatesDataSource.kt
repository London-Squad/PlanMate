package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.ProjectMongoDto
import data.dto.TaskStateMongoDto
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toTaskStateDto
import data.repositories.dtoMappers.toTaskStateMongoDto
import kotlinx.coroutines.flow.firstOrNull
import logic.entities.TaskState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.exceptions.TaskNotFoundException
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TaskStatesRepository
import org.bson.Document
import java.util.UUID


class MongoDBTaskStatesDataSource(
    private val taskStatesCollection: MongoCollection<TaskStateMongoDto>
) : TaskStatesRepository {

    override suspend fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        return try {
            val filter = Filters.and(
                Filters.eq(TaskStateMongoDto::projectId.name, projectId.toString()),
                if (includeDeleted) Filters.exists(TaskStateMongoDto::isDeleted.name)
                else Filters.eq(TaskStateMongoDto::isDeleted.name, false)
            )
            taskStatesCollection.find(filter).map(TaskStateMongoDto::toTaskState).toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve task states: ${e.message}")
        }
    }

    override suspend fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        return try {
            val filter = Filters.and(
                Filters.eq("_id", stateId.toString()),
                if (includeDeleted) Filters.exists(TaskStateMongoDto::isDeleted.name)
                else Filters.eq(TaskStateMongoDto::isDeleted.name, false)
            )
            taskStatesCollection.find(filter).firstOrNull()
                ?.toTaskState()
                ?: throw TaskStateNotFoundException()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve task state: ${e.message}")
        }
    }

    override suspend fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        try {
            taskStatesCollection.insertOne(taskState.toTaskStateMongoDto(projectId))
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add task state: ${e.message}")
        }
    }

    override suspend fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        try {
            val filter = Filters.and(
                Filters.eq("_id", stateId.toString()),
                Filters.eq(TaskStateMongoDto::isDeleted.name, false)
            )

            taskStatesCollection.updateOne(
                filter, Updates.set(TaskStateMongoDto::title.name, newTitle)
            ).apply {
                if (matchedCount == 0L) {
                    throw TaskStateNotFoundException("Task state with ID $stateId not found")
                }
            }

            taskStatesCollection.updateOne(
                Filters.eq("_id", stateId.toString()), Updates.set(TaskStateMongoDto::title.name, newTitle)
            )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state title: ${e.message}")
        }
    }

    override suspend fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        try {
            val filter = Filters.and(
                Filters.eq("_id", stateId.toString()),
                Filters.eq(TaskStateMongoDto::isDeleted.name, false)
            )

            taskStatesCollection.updateOne(
                filter, Updates.set(ProjectMongoDto::description.name, newDescription)
            ).apply {
                if (matchedCount == 0L) {
                    throw TaskStateNotFoundException("Task state with ID $stateId not found")
                }
            }

            taskStatesCollection.updateOne(
                Filters.eq("_id", stateId.toString()),
                Updates.set(TaskStateMongoDto::title.name, newDescription)
            )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state description: ${e.message}")
        }
    }

    override suspend fun deleteTaskState(stateId: UUID) {
        try {
            val filters = Filters.and(
                Filters.eq("_id", stateId.toString()),
                Filters.eq(TaskStateMongoDto::isDeleted.name, false)
            )

            taskStatesCollection.updateOne(
                filters, Updates.set(TaskStateMongoDto::isDeleted.name, true)
            ).apply {
                if (matchedCount.toInt() == 0) {
                    throw TaskNotFoundException("Task state with ID $stateId not found")
                }
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete task state: ${e.message}")
        }
    }
}