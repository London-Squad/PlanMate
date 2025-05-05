package logic.repositories

import logic.entities.State
import java.util.UUID

interface TasksStatesRepository {

    fun getTasksStatesByProjectId(projectId: UUID, includeDeleted: Boolean = false): List<State>

    fun getTaskStateById(stateId: UUID, includeDeleted: Boolean = false): State

    fun addNewTaskState(state: State, projectId: UUID)

    fun editTaskStateTitle(stateId: UUID, newTitle: String)

    fun editTaskStateDescription(stateId: UUID, newDescription: String)

    fun deleteTaskState(stateId: UUID)
}