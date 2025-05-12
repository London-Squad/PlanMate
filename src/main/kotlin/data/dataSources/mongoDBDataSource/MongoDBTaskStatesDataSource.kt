package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toTaskStateDto
import logic.entities.TaskState
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.exceptions.TaskNotFoundException
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TaskStatesRepository
import org.bson.Document
import java.util.UUID

class MongoDBTaskStatesDataSource(
    private val taskStatesCollection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse,
) : TaskStatesRepository {

    override fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        return try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.PROJECT_ID_FIELD, projectId.toString()),
                if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
                else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )
            taskStatesCollection.find(filter).map { doc ->
                mongoParser.documentToTaskStateDto(doc).toTaskState()
            }.toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve task states: ${e.message}")
        }
    }

    override fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        return try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                if (includeDeleted) Filters.exists(MongoDBParse.IS_DELETED_FIELD)
                else Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )
            taskStatesCollection.find(filter).first()
                ?.let { mongoParser.documentToTaskStateDto(it).toTaskState() }
                ?: throw TaskStateNotFoundException()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve task state: ${e.message}")
        }
    }

    override fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        try {
            val doc = mongoParser.taskStateDtoToDocument(taskState.toTaskStateDto(projectId))
            taskStatesCollection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add task state: ${e.message}")
        }
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            taskStatesCollection.updateOne(
                filter, Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
            ).apply {
                if (matchedCount == 0L) {
                    throw TaskStateNotFoundException("Task state with ID $stateId not found")
                }
            }

            taskStatesCollection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()), Updates.set(MongoDBParse.TITLE_FIELD, newTitle)
            )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state title: ${e.message}")
        }
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        try {
            val filter = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            taskStatesCollection.updateOne(
                filter, Updates.set(MongoDBParse.DESCRIPTION_FIELD, newDescription)
            ).apply {
                if (matchedCount == 0L) {
                    throw TaskStateNotFoundException("Task state with ID $stateId not found")
                }
            }

            taskStatesCollection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                Updates.set(MongoDBParse.TITLE_FIELD, newDescription)
            )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to edit task state description: ${e.message}")
        }
    }

    override fun deleteTaskState(stateId: UUID) {
        try {
            val filters = Filters.and(
                Filters.eq(MongoDBParse.ID_FIELD, stateId.toString()),
                Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            )

            taskStatesCollection.updateOne(
                filters, Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            ).apply {
                if (matchedCount.toInt() == 0) {
                    throw TaskNotFoundException("Task state with ID $stateId not found")
                }
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete task state: ${e.message}")
        }
    }
}