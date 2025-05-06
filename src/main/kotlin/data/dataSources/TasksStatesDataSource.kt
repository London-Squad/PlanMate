package data.dataSources

import data.dto.TaskStateDto
import java.util.UUID

interface TasksStatesDataSource {
    fun getAllTasksStates(): List<TaskStateDto>

    fun addNewTaskState(taskStateDto: TaskStateDto)

    fun editTaskStateTitle(stateId: UUID, newTitle: String)

    fun editTaskStateDescription(stateId: UUID, newDescription: String)

    fun deleteTaskState(stateId: UUID)
}