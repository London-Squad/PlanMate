package data.mongoDBDataSource

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.MongoCollection
import data.dataSources.TasksDataSource
import data.dto.TaskDto
import org.bson.Document
import java.util.UUID

class MongoDBTasksDataSource(
    private val collection: MongoCollection<Document> = DatabaseConnection.getTasksCollection()
) : TasksDataSource {

    override fun getAllTasks(): List<TaskDto> {
        return collection.find().map { doc ->
            TaskDto(
                id = UUID.fromString(doc.getString(ID_FIELD)),
                title = doc.getString(TITLE_FIELD),
                description = doc.getString(DESCRIPTION_FIELD),
                stateId = UUID.fromString(doc.getString(STATE_ID_FIELD)),
                projectId = UUID.fromString(doc.getString(PROJECT_ID_FIELD)),
                isDeleted = doc.getBoolean(IS_DELETED_FIELD) ?: false
            )
        }.toList()
    }

    override fun addNewTask(taskDto: TaskDto) {
        val doc = Document(ID_FIELD, taskDto.id.toString())
            .append(TITLE_FIELD, taskDto.title)
            .append(DESCRIPTION_FIELD, taskDto.description)
            .append(STATE_ID_FIELD, taskDto.stateId.toString())
            .append(PROJECT_ID_FIELD, taskDto.projectId.toString())
            .append(IS_DELETED_FIELD, taskDto.isDeleted)
        collection.insertOne(doc)
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        collection.updateOne(Filters.eq(ID_FIELD, taskId.toString()), Updates.set(TITLE_FIELD, newTitle))
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        collection.updateOne(Filters.eq(ID_FIELD, taskId.toString()), Updates.set(DESCRIPTION_FIELD, newDescription))
    }

    override fun editTaskState(taskId: UUID, newStateId: UUID) {
        collection.updateOne(Filters.eq(ID_FIELD, taskId.toString()), Updates.set(STATE_ID_FIELD, newStateId.toString()))
    }

    override fun deleteTask(taskId: UUID) {
        collection.deleteOne(Filters.eq(ID_FIELD, taskId.toString()))
    }

    companion object {
        private const val ID_FIELD = "id"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val STATE_ID_FIELD = "stateId"
        private const val PROJECT_ID_FIELD = "projectId"
        private const val IS_DELETED_FIELD = "isDeleted"
    }
}