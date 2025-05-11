package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.TaskStateDto
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import logic.exceptions.TaskStateNotFoundException
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import org.bson.Document
import java.util.*

class MongoDBTaskStatesDataSource(
    private val collection: MongoCollection<Document>, private val mongoParser: MongoDBParse
) : TaskStatesDataSource {

    override fun getAllTasksStates(includeDeleted: Boolean): List<TaskStateDto> {
        return try {
            collection.find().map { doc -> mongoParser.documentToTaskStateDto(doc) }.toList()
                .filter { if (includeDeleted) true else !it.isDeleted }
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve task states: ${e.message}")
        }
    }

    override fun addNewTaskState(taskStateDto: TaskStateDto) {
        try {
            val doc = mongoParser.taskStateDtoToDocument(taskStateDto)
            collection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add task state: ${e.message}")
        }
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        try {
            val result = collection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskStateNotFoundException("Task State with ID $stateId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state title: ${e.message}")
        }
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        try {
            val result = collection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskStateNotFoundException("Task State with ID $stateId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state description: ${e.message}")
        }
    }

    override fun deleteTaskState(stateId: UUID) {
        try {
            val result = collection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskStateNotFoundException("Task State with ID $stateId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete task state: ${e.message}")
        }
    }
}