package data.mongoDBDataSource


import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.TasksStatesDataSource
import data.dto.TaskStateDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bson.Document
import java.util.UUID


class MongoDBTaskStatesDataSource(
    private val collection: MongoCollection<Document> = DatabaseConnection.getUsersCollection()
) : TasksStatesDataSource {

    companion object {
        private const val ID_FIELD = "id"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val PROJECT_ID_FIELD = "projectId"
        private const val IS_DELETED_FIELD = "isDeleted"
    }

    override fun getAllTasksStates(): List<TaskStateDto> {
        var result: List<TaskStateDto> = emptyList()
        CoroutineScope(Dispatchers.IO).launch {
            result = collection.find().map { doc ->
                TaskStateDto(
                    id = UUID.fromString(doc.getString(ID_FIELD)),
                    title = doc.getString(TITLE_FIELD),
                    description = doc.getString(DESCRIPTION_FIELD),
                    projectId = UUID.fromString(doc.getString(PROJECT_ID_FIELD)),
                    isDeleted = doc.getBoolean(IS_DELETED_FIELD) ?: false
                )
            }.toList()
        }
        return result
    }

    override fun addNewTaskState(taskStateDto: TaskStateDto) {
        CoroutineScope(Dispatchers.IO).launch {
            val doc = Document(ID_FIELD, taskStateDto.id.toString())
                .append(TITLE_FIELD, taskStateDto.title)
                .append(DESCRIPTION_FIELD, taskStateDto.description)
                .append(PROJECT_ID_FIELD, taskStateDto.projectId.toString())
                .append(IS_DELETED_FIELD, taskStateDto.isDeleted)
            collection.insertOne(doc)
        }
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        CoroutineScope(Dispatchers.IO).launch {
            collection.updateOne(Filters.eq(ID_FIELD, stateId.toString()), Updates.set(TITLE_FIELD, newTitle))
        }
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        CoroutineScope(Dispatchers.IO).launch {

            collection.updateOne(
                Filters.eq(ID_FIELD, stateId.toString()),
                Updates.set(DESCRIPTION_FIELD, newDescription)
            )
        }
    }

    override fun deleteTaskState(stateId: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            collection.deleteOne(Filters.eq(ID_FIELD, stateId.toString()))
        }
    }
}