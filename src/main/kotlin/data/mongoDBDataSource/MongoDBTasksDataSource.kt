package data.mongoDBDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.MongoCollection
import data.dataSources.TasksDataSource
import data.dto.TaskDto
import org.bson.Document
import java.util.UUID

class MongoDBTasksDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : TasksDataSource {

    override fun getAllTasks(): List<TaskDto> {
        return collection.find().map { doc ->
            mongoParser.documentToTaskDto(doc)
        }.toList()
    }

    override fun addNewTask(taskDto: TaskDto) {
        val doc = mongoParser.taskDtoToDocument(taskDto)
        collection.insertOne(doc)
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        )
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
        )
    }

    override fun editTaskState(taskId: UUID, newStateId: UUID) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.STATE_ID_FIELD, newStateId.toString())
        )
    }

    override fun deleteTask(taskId: UUID) {
        collection.deleteOne(Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()))
    }
}