package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBQueryHandler
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.repositories.TaskRepository
import java.util.*

class MongoDBTasksDataSource(
    private val taskQueryHandler: MongoDBQueryHandler,
    private val mongoParser: MongoDBParse,
) : TaskRepository {

    override suspend fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        val filters = Filters.and(
            Filters.eq(MongoDBParse.PROJECT_ID_FIELD, projectId.toString()),
            if (includeDeleted) Filters.empty()
            else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        return taskQueryHandler.fetchManyFromCollection(filters).map { mongoParser.documentToTaskDto(it).toTask() }
    }

    override suspend fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        val filters = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            if (includeDeleted) Filters.empty()
            else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        return taskQueryHandler.fetchOneFromCollection(filters).let(mongoParser::documentToTaskDto).toTask()
    }

    override suspend fun addNewTask(task: Task, projectId: UUID) {
        val doc = mongoParser.taskDtoToDocument(task.toTaskDto(projectId))
        taskQueryHandler.insertToCollection(doc)
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.updateCollection(MongoDBParse.TITLE_FIELD, newTitle, filters)
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.updateCollection(MongoDBParse.DESCRIPTION_FIELD, newDescription, filters)
    }

    override suspend fun editTaskState(taskId: UUID, newStateId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.updateCollection(MongoDBParse.STATE_ID_FIELD, newStateId.toString(), filters)
    }

    override suspend fun deleteTask(taskId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.softDeleteFromCollection(filters)
    }
}