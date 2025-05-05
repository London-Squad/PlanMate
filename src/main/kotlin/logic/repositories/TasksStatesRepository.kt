package logic.repositories

import logic.entities.TaskState
import java.util.UUID

interface TasksStatesRepository {

    fun getTasksStatesByProjectId(projectId: UUID, includeDeleted: Boolean = false): List<TaskState>

    fun getTaskStateById(stateId: UUID, includeDeleted: Boolean = false): TaskState

    fun addNewTaskState(taskState: TaskState, projectId: UUID)

    fun editTaskStateTitle(stateId: UUID, newTitle: String)

    fun editTaskStateDescription(stateId: UUID, newDescription: String)

    fun deleteTaskState(stateId: UUID)
}