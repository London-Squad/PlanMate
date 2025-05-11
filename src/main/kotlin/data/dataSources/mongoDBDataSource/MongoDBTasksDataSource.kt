package data.dataSources.mongoDBDataSource
import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.TaskDto
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.entities.TaskState
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import org.bson.Document
import java.util.*

class MongoDBTasksDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : TaskRepository {

    override fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        val filter = Filters.and(
            Filters.eq(MongoDBParse.PROJECT_ID_FIELD, projectId.toString()),
            if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
            else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        return collection.find(filter).map { doc ->
            mongoParser.documentToTaskDto(doc).toTask()
        }.toList()

    }

    override fun getTasksByTaskStateID(
        taskStateId: UUID,
        includeDeleted: Boolean
    ): List<Task> {
        return getAllTasks(includeDeleted)
            .filter { it.stateId == taskStateId }
            .map(TaskDto::toTask)
    }


    private fun getAllTasks(includeDeleted: Boolean): List<TaskDto> {
        try {
            return collection.find().map { doc ->
            mongoParser.documentToTaskDto(doc)
        }.toList()
        } catch (e: MongoException) {
        throw RetrievingDataFailureException("Failed to retrieve tasks: ${e.message}")
    }
    }

    override fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        val filter = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
            else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        return collection.find(filter).first()?.let { mongoParser.documentToTaskDto(it).toTask() }
            ?: throw TaskNotFoundException()
    }

    override fun addNewTask(task: Task, projectId: UUID) {
        try{
        val doc = mongoParser.taskDtoToDocument(task.toTaskDto(projectId))
        collection.insertOne(doc)
    } catch (e: MongoException) {
        throw StoringDataFailureException("Failed to add task: ${e.message}")
    }
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        try {
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()), Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
        )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task title: ${e.message}")
        }
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        try {
            val result = collection.updateOne(
                Filters.and(
                    Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
                    Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
                ), Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
            )
            if (result.matchedCount.toInt() == 0) {
                throw TaskNotFoundException("Task with ID $taskId not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task description: ${e.message}")
        }
    }

    override fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        try{
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()),
            Updates.set(MongoDBParse.STATE_ID_FIELD, newTaskState.id.toString()))
    } catch (e: MongoException) {
        throw StoringDataFailureException("Failed to edit task state: ${e.message}")
    }

    }

    override fun deleteTask(taskId: UUID) {
        try{
        collection.updateOne(
            Filters.eq(MongoDBParse.ID_FIELD, taskId.toString()), Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
        )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete task: ${e.message}")
        }
    }
}