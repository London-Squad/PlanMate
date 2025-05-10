package data.repositories.dataSourceInterfaces

import data.dto.TaskStateDto
import java.util.UUID

interface TaskStatesDataSource {
    suspend fun getAllTasksStates(includeDeleted: Boolean): List<TaskStateDto>

    suspend fun createDefaultTaskStatesForProject(projectId: UUID): List<TaskStateDto>

    suspend fun addNewTaskState(taskStateDto: TaskStateDto)

    suspend fun editTaskStateTitle(stateId: UUID, newTitle: String)

    suspend fun editTaskStateDescription(stateId: UUID, newDescription: String)

    suspend fun deleteTaskState(stateId: UUID)
}