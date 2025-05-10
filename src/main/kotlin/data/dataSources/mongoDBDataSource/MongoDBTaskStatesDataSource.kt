package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.dataSources.defaultTaskStatesTitleAndDescription
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.TaskStateDto
import org.bson.Document
import java.util.UUID

class MongoDBTaskStatesDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : TaskStatesDataSource {

    override fun getAllTasksStates(includeDeleted: Boolean): List<TaskStateDto> {
        return collection.find().map { doc ->
            mongoParser.documentToTaskStateDto(doc)
        }.toList()
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override fun addNewTaskState(taskStateDto: TaskStateDto) {
        val doc = mongoParser.taskStateDtoToDocument(taskStateDto)
        collection.insertOne(doc)
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
            Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        )
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
            Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
        )
    }

    override fun deleteTaskState(stateId: UUID) {
        collection.deleteOne(Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()))
    }
}