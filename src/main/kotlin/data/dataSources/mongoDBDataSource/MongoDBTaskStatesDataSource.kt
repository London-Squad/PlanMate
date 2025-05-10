package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.defaultTaskStatesTitleAndDescription
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.dto.TaskStateDto
import kotlinx.coroutines.flow.map
import org.bson.Document
import java.util.UUID
import kotlinx.coroutines.flow.toList

class MongoDBTaskStatesDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : TaskStatesDataSource {

    override suspend fun getAllTasksStates(includeDeleted: Boolean): List<TaskStateDto> {
        return collection.find()
            .map { doc -> mongoParser.documentToTaskStateDto(doc) }
            .toList()
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override suspend fun createDefaultTaskStatesForProject(projectId: UUID): List<TaskStateDto> {
        return defaultTaskStatesTitleAndDescription.map {
            TaskStateDto(
                id = UUID.randomUUID(), title = it[0], description = it[1],
                projectId = projectId,
                isDeleted = false
            )
        }
    }

    override suspend fun addNewTaskState(taskStateDto: TaskStateDto) {
        val doc = mongoParser.taskStateDtoToDocument(taskStateDto)
        collection.insertOne(doc).let { }
    }

    override suspend fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
            Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        ).let { }
    }

    override suspend fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
            Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
        ).let { }
    }

    override suspend fun deleteTaskState(stateId: UUID) {
        collection.deleteOne(Filters.eq(MongoDBParse.ID_FIELD, stateId.toString())).let { }
    }
}