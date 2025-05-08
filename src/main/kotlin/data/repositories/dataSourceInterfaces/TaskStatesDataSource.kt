package data.repositories.dataSourceInterfaces

import data.dto.TaskStateDto
import java.util.UUID

interface TaskStatesDataSource {
    fun getAllTasksStates(includeDeleted: Boolean): List<TaskStateDto>

    fun createDefaultTaskStatesForProject(projectId: UUID): List<TaskStateDto>

    fun addNewTaskState(taskStateDto: TaskStateDto)

    fun editTaskStateTitle(stateId: UUID, newTitle: String)

    fun editTaskStateDescription(stateId: UUID, newDescription: String)

    fun deleteTaskState(stateId: UUID)
}