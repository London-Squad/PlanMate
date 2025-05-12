package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.entities.TaskState
import data.dto.TaskDto
import data.repositories.dataSourceInterfaces.TasksDataSource
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.exceptions.TaskNotFoundException
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import org.bson.Document
import java.util.*

class MongoDBTasksDataSource(
    private val tasksCollection: MongoCollection<Document>, private val mongoParser: MongoDBParse
) : TaskRepository {

    override suspend fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        return try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.PROJECT_ID_FIELD, projectId.toString()),
                if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
                else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )
            tasksCollection.find(filter).map { doc ->
                mongoParser.documentToTaskDto(doc).toTask()
            }.toList()

        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve tasks: ${e.message}")
        }
    }

    override suspend fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
                else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )
            tasksCollection.find(filter).first()?.let { mongoParser.documentToTaskDto(it).toTask() }
                ?: throw TaskNotFoundException()

        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve task: ${e.message}")
        }
    }

    override suspend fun addNewTask(task: Task, projectId: UUID) {
        try {
            val doc = mongoParser.taskDtoToDocument(task.toTaskDto(projectId))
            tasksCollection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add task: ${e.message}")
        }
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            tasksCollection.updateOne(
                filter, Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
            ).apply {
                if (matchedCount == 0L) {
                    throw TaskNotFoundException("Task with ID $taskId not found")
                }
            }

            tasksCollection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()), Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
            )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task title: ${e.message}")
        }
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        try {
            val filters = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            tasksCollection.updateOne(
                filters, Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
            ).apply {
                if (matchedCount.toInt() == 0) {
                    throw TaskNotFoundException("Task with ID $taskId not found")
                }
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task description: ${e.message}")
        }
    }

    override suspend fun editTaskState(taskId: UUID, newStateId: UUID) {
        try {
            val filters = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            tasksCollection.updateOne(
                filters, Updates.set(MongoDBParse.STATE_ID_FIELD, newStateId.toString())
            ).apply {
                if (matchedCount.toInt() == 0) {
                    throw TaskNotFoundException("Task with ID $taskId not found")
                }
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state: ${e.message}")
        }
    }

    override suspend fun deleteTask(taskId: UUID) {
        try {
            val filters = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            tasksCollection.updateOne(
                filters, Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            ).apply {
                if (matchedCount.toInt() == 0) {
                    throw TaskNotFoundException("Task with ID $taskId not found")
                }
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete task: ${e.message}")
        }
    }
}