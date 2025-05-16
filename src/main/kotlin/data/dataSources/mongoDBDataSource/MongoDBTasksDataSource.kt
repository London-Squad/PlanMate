package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.TaskMongoDto
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import data.repositories.dtoMappers.toTaskMongoDto
import logic.entities.Task
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import org.bson.Document
import java.util.*

class MongoDBTasksDataSource(
    private val tasksCollection: MongoCollection<TaskMongoDto>
) : TaskRepository {

    override suspend fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        return try {
            val filter = Filters.and(
                Filters.eq(TaskMongoDto::projectId.name, projectId.toString()),
                if (includeDeleted) Filters.exists(TaskMongoDto::isDeleted.name)
                else Filters.eq(TaskMongoDto::isDeleted.name, false)
            )
            tasksCollection.find(filter)
                .map(TaskMongoDto::toTask)
                .toList()

        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve tasks: ${e.message}")
        }
    }

    override suspend fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return try {
            val filter = Filters.and(
                Filters.eq("_id", taskId.toString()),
                if (includeDeleted) Filters.exists(TaskMongoDto::isDeleted.name)
                else Filters.eq(TaskMongoDto::isDeleted.name, false)
            )
            tasksCollection.find(filter).firstOrNull()
                ?.toTask()
                ?: throw TaskNotFoundException()

        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve task: ${e.message}")
        }
    }

    override suspend fun addNewTask(task: Task, projectId: UUID) {
        try {
            tasksCollection.insertOne(task.toTaskMongoDto(projectId))
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add task: ${e.message}")
        }
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        try {
            val filter = Filters.and(
                Filters.eq("_id", taskId.toString()),
                Filters.eq(TaskMongoDto::isDeleted.name, false)
            )

            tasksCollection.updateOne(
                filter, Updates.set(TaskMongoDto::title.name, newTitle)
            ).apply {
                if (matchedCount == 0L) {
                    throw TaskNotFoundException("Task with ID $taskId not found")
                }
            }

            tasksCollection.updateOne(
                Filters.eq("_id", taskId.toString()), Updates.set(TaskMongoDto::title.name, newTitle)
            )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task title: ${e.message}")
        }
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        try {
            val filters = Filters.and(
                Filters.eq("_id", taskId.toString()),
                Filters.eq(TaskMongoDto::isDeleted.name, false)
            )

            tasksCollection.updateOne(
                filters, Updates.set(TaskMongoDto::description.name, newDescription)
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
                Filters.eq("_id", taskId.toString()),
                Filters.eq(TaskMongoDto::isDeleted.name, false)
            )

            tasksCollection.updateOne(
                filters, Updates.set(TaskMongoDto::stateId.name, newStateId.toString())
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
                Filters.eq("_id", taskId.toString()),
                Filters.eq(TaskMongoDto::isDeleted.name, false)
            )

            tasksCollection.updateOne(
                filters, Updates.set(TaskMongoDto::isDeleted.name, true)
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