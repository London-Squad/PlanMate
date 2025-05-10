package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.TasksDataSource
import data.dto.TaskDto
import kotlinx.coroutines.flow.map
import org.bson.Document
import java.util.UUID
import kotlinx.coroutines.flow.toList

class MongoDBTasksDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : TasksDataSource {

    override suspend fun getAllTasks(includeDeleted: Boolean): List<TaskDto> {
        return collection.find()
            .map { doc -> mongoParser.documentToTaskDto(doc) }
            .toList()
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override suspend fun addNewTask(taskDto: TaskDto) {
        val doc = mongoParser.taskDtoToDocument(taskDto)
        collection.insertOne(doc).let { }
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        ).let { }
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
        ).let { }
    }

    override suspend fun editTaskState(taskId: UUID, newStateId: UUID) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.STATE_ID_FIELD, newStateId.toString())
        ).let { }
    }

    override suspend fun deleteTask(taskId: UUID) {
        collection.deleteOne(Filters.eq(MongoDBParse.ID_FIELD, taskId.toString())).let { }
    }
}