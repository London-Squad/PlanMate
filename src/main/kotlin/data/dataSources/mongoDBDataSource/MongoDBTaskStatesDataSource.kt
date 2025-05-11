package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toTaskStateDto
import logic.entities.TaskState
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TaskStatesRepository
import org.bson.Document
import java.util.UUID

class MongoDBTaskStatesDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse,
) : TaskStatesRepository {

    override fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        val filter = Filters.and(
            Filters.eq(MongoDBParse.PROJECT_ID_FIELD, projectId.toString()),
            if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
            else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        return collection.find(filter).map { doc ->
            mongoParser.documentToTaskStateDto(doc).toTaskState()
        }.toList()
    }

    override fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        val filter = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
            if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
            else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        return collection.find(filter).first()
            ?.let { mongoParser.documentToTaskStateDto(it).toTaskState() }
            ?: throw TaskStateNotFoundException()
    }

    override fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        val doc = mongoParser.taskStateDtoToDocument(taskState.toTaskStateDto(projectId))
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
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
            Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
        )

    }
}