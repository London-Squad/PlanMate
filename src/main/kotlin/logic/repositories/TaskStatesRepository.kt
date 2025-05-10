package logic.repositories

import logic.entities.TaskState
import java.util.UUID

interface TaskStatesRepository {

    suspend fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean = false): List<TaskState>

    suspend fun getTaskStateById(stateId: UUID, includeDeleted: Boolean = false): TaskState

    suspend fun addNewTaskState(taskState: TaskState, projectId: UUID)

    suspend fun getDefaultTaskStates(projectId: UUID): List<TaskState>

    suspend fun editTaskStateTitle(stateId: UUID, newTitle: String)

    suspend fun editTaskStateDescription(stateId: UUID, newDescription: String)

    suspend fun deleteTaskState(stateId: UUID)
}