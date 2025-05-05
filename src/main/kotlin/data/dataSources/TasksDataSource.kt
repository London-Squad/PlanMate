package data.dataSources

import data.dto.TaskDto
import java.util.*

interface TasksDataSource {
    fun getAllTasks(): List<TaskDto>

    fun addNewTask(taskDto: TaskDto)

    fun editTaskTitle(taskId: UUID, newTitle: String)

    fun editTaskDescription(taskId: UUID, newDescription: String)

    fun editTaskState(taskId: UUID, newStateId: UUID)

    fun deleteTask(taskId: UUID)
}