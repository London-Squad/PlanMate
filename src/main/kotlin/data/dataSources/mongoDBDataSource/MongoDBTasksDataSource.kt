package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.TaskDto
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.entities.TaskState
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import org.bson.Document
import java.util.UUID

class MongoDBTasksDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : TaskRepository {

    override fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        return getAllTasks(includeDeleted)
            .filter { it.projectId == projectId }
            .map(TaskDto::toTask)
    }

    override fun getTasksByTaskStateID(taskStateId: UUID, includeDeleted: Boolean): List<Task> {
        return getAllTasks(includeDeleted)
            .filter { it.stateId == taskStateId }
            .map(TaskDto::toTask)
    }

    override fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return getAllTasks(includeDeleted)
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == taskId }
            ?.toTask()
            ?: throw TaskNotFoundException()
    }

    private fun getAllTasks(includeDeleted: Boolean): List<TaskDto> {
        return collection.find().map { doc ->
            mongoParser.documentToTaskDto(doc)
        }.toList()
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override fun addNewTask(task: Task, projectId: UUID) {
        val doc = mongoParser.taskDtoToDocument(task.toTaskDto(projectId))
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

    override fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.STATE_ID_FIELD, newTaskState.id.toString())
        )
    }

    override fun deleteTask(taskId: UUID) {
        collection.deleteOne(Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()))
    }
}