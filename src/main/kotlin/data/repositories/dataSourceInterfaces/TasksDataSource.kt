package data.repositories.dataSourceInterfaces

import data.dto.TaskDto
import java.util.*

interface TasksDataSource {
    suspend fun getAllTasks(includeDeleted: Boolean): List<TaskDto>

    suspend fun addNewTask(taskDto: TaskDto)

    suspend fun editTaskTitle(taskId: UUID, newTitle: String)

    suspend fun editTaskDescription(taskId: UUID, newDescription: String)

    suspend fun editTaskState(taskId: UUID, newStateId: UUID)

    suspend fun deleteTask(taskId: UUID)
}