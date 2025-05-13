package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBQueryHandler
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toTaskStateDto
import logic.entities.TaskState
import logic.repositories.TaskStatesRepository
import java.util.UUID


class MongoDBTaskStatesDataSource(
    private val taskStatesQueryHandler: MongoDBQueryHandler,
    private val mongoParser: MongoDBParser,
) : TaskStatesRepository {

    override suspend fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.PROJECT_ID_FIELD, projectId.toString()),
            if (includeDeleted) Filters.exists(MongoDBParser.IS_DELETED_FIELD)
            else Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        return taskStatesQueryHandler.fetchManyFromCollection(filters)
            .map { mongoParser.documentToTaskStateDto(it).toTaskState() }
    }

    override suspend fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, stateId.toString()),
            if (includeDeleted) Filters.exists(MongoDBParser.IS_DELETED_FIELD)
            else Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        return taskStatesQueryHandler.fetchOneFromCollection(filters).let(mongoParser::documentToTaskStateDto)
            .toTaskState()
    }

    override suspend fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        val doc = mongoParser.taskStateDtoToDocument(taskState.toTaskStateDto(projectId))
        taskStatesQueryHandler.insertToCollection(doc)
    }

    override suspend fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, stateId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        taskStatesQueryHandler.updateCollection(MongoDBParser.TITLE_FIELD, newTitle, filters)
    }

    override suspend fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, stateId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        taskStatesQueryHandler.updateCollection(MongoDBParser.DESCRIPTION_FIELD, newDescription, filters)
    }

    override suspend fun deleteTaskState(stateId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, stateId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        taskStatesQueryHandler.softDeleteFromCollection(filters)
    }
}