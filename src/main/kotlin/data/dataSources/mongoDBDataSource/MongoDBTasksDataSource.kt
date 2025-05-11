package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.TaskDto
import data.repositories.dataSourceInterfaces.TasksDataSource
import logic.exceptions.TaskNotFoundException
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import org.bson.Document
import java.util.*

class MongoDBTasksDataSource(
    private val collection: MongoCollection<Document>, private val mongoParser: MongoDBParse
) : TasksDataSource {

    override fun getAllTasks(includeDeleted: Boolean): List<TaskDto> {
        try {
            return collection.find().map { doc ->
                mongoParser.documentToTaskDto(doc)
            }.toList().filter { if (includeDeleted) true else !it.isDeleted }
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve tasks: ${e.message}")
        }
    }

    override fun addNewTask(taskDto: TaskDto) {
        try {
            val doc = mongoParser.taskDtoToDocument(taskDto)
            collection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add task: ${e.message}")
        }
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        try {
            val result = collection.updateOne(
                Filters.and(
                    Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                    Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
                ), Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskNotFoundException("Task with ID $taskId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task title: ${e.message}")
        }
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        try {
            val result = collection.updateOne(
                Filters.and(
                    Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                    Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
                ), Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskNotFoundException("Task with ID $taskId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task description: ${e.message}")
        }
    }

    override fun editTaskState(taskId: UUID, newStateId: UUID) {
        try {
            val result = collection.updateOne(
                Filters.and(
                    Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                    Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
                ), Updates.set(MongoDBParse.STATE_ID_FIELD, newStateId.toString())
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskNotFoundException("Task with ID $taskId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state: ${e.message}")
        }
    }

    override fun deleteTask(taskId: UUID) {
        try {
            val result = collection.updateOne(
                Filters.and(
                    Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                    Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
                ), Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskNotFoundException("Task with ID $taskId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete task: ${e.message}")
        }
    }
}