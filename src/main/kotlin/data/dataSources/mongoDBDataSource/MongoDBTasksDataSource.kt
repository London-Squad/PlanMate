package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBQueryHandler
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.repositories.TaskRepository
import java.util.*

class MongoDBTasksDataSource(
    private val taskQueryHandler: MongoDBQueryHandler,
    private val mongoParser: MongoDBParser,
) : TaskRepository {

    override suspend fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.PROJECT_ID_FIELD, projectId.toString()),
            if (includeDeleted) Filters.empty()
            else Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        return taskQueryHandler.fetchManyFromCollection(filters).map { mongoParser.documentToTaskDto(it).toTask() }
    }

    override suspend fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, taskId.toString()),
            if (includeDeleted) Filters.empty()
            else Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        return taskQueryHandler.fetchOneFromCollection(filters).let(mongoParser::documentToTaskDto).toTask()
    }

    override suspend fun getTaskTitleById(taskId: UUID): String {
        return getTaskByID(taskId,true).title
    }

    override suspend fun addNewTask(task: Task, projectId: UUID) {
        task
            .toTaskDto(projectId)
            .let(mongoParser::taskDtoToDocument)
            .also { taskQueryHandler.insertToCollection(it) }
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.updateCollection(MongoDBParser.TITLE_FIELD, newTitle, filters)
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.updateCollection(MongoDBParser.DESCRIPTION_FIELD, newDescription, filters)
    }

    override suspend fun editTaskState(taskId: UUID, newStateId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.updateCollection(MongoDBParser.STATE_ID_FIELD, newStateId.toString(), filters)
    }

    override suspend fun deleteTask(taskId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, taskId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        taskQueryHandler.softDeleteFromCollection(filters)
    }
}