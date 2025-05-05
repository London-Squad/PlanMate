package logic.repositories

import logic.entities.State
import java.util.UUID

interface TasksStatesRepository {

    fun getTasksStatesByProjectId(projectId: UUID): List<State>

    fun getStateById(stateId: UUID): State

    fun addNewTaskState(state: State, projectId: UUID)

    fun editTaskStateTitle(stateId: UUID, newTitle: String)

    fun editTaskStateDescription(stateId: UUID, newDescription: String)

    fun deleteTaskState(stateId: UUID)
}