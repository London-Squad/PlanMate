package data.repositoriesImpl

import data.entitiesData.DtoMapper
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

    override fun getTasksByProjectID(projectId: UUID): List<Task> {
        return tasksDataSource.getAllTasks()
            .filter { !it.isDeleted && it.projectId == projectId }
            .map { taskData ->
                val taskState = tasksStatesRepository.getStateById(taskData.stateId)
                mapper.mapToTask(taskData, taskState)
            }
    }

    override fun getTaskByID(taskId: UUID): Task {
        return tasksDataSource.getAllTasks()
            .firstOrNull { !it.isDeleted && it.id == taskId }
            ?.let {
                val taskState = tasksStatesRepository.getStateById(it.stateId)
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