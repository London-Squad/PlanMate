package data.dataSources

import data.entitiesData.TaskData
import java.util.*

interface TasksDataSource {
    fun getAllTasks(): List<TaskData>

    fun addNewTask(taskData: TaskData)

    fun editTaskTitle(taskId: UUID, newTitle: String)

    fun editTaskDescription(taskId: UUID, newDescription: String)

    fun editTaskState(taskId: UUID, newStateId: UUID)

    fun deleteTask(taskId: UUID)
}