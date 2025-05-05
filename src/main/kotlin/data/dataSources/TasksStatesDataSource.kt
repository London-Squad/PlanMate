package data.dataSources

import data.entitiesData.TaskStateData
import java.util.UUID

interface TasksStatesDataSource {
    fun getAllTasksStates(): List<TaskStateData>

    fun addNewTaskState(taskStateData: TaskStateData)

    fun editTaskStateTitle(stateId: UUID, newTitle: String)

    fun editTaskStateDescription(stateId: UUID, newDescription: String)

    fun deleteTaskState(stateId: UUID)
}