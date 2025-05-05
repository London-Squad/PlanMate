package data.repositoriesImpl

import data.csvDataSource.DtoMapper
import data.dataSources.TasksDataSource
import logic.entities.State
import logic.entities.Task
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import logic.repositories.TasksStatesRepository
import java.util.*

class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource,
    private val tasksStatesRepository: TasksStatesRepository,
    private val mapper: DtoMapper
) : TaskRepository {

    override fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        return tasksDataSource.getAllTasks()
            .filter { if (includeDeleted) true else !it.isDeleted }
            .filter { it.projectId == projectId }
            .map { taskData ->
                val taskState = tasksStatesRepository.getTaskStateById(taskData.stateId)
                mapper.mapToTask(taskData, taskState)
            }
    }

    override fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return tasksDataSource.getAllTasks()
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == taskId }
            ?.let {
                val taskState = tasksStatesRepository.getTaskStateById(it.stateId)
                return mapper.mapToTask(it, taskState)
            } ?: throw TaskNotFoundException()
    }


    override fun addNewTask(task: Task, projectId: UUID) {
        tasksDataSource.addNewTask(
            mapper.mapToTaskData(task, projectId)
        )
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        tasksDataSource.editTaskTitle(taskId, newTitle)
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        tasksDataSource.editTaskDescription(taskId, newDescription)
    }

    override fun editTaskState(taskId: UUID, newState: State) {
        tasksDataSource.editTaskState(taskId, newState.id)
    }

    override fun deleteTask(taskId: UUID) {
        tasksDataSource.deleteTask(taskId)
    }
}