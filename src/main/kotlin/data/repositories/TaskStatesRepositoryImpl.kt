package data.repositories

import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toTaskStateDto
import logic.entities.Task
import logic.entities.TaskState
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import java.util.*

class TaskStatesRepositoryImpl(
    private val taskStatesDataSource: TaskStatesDataSource,
    private val tasksRepository: TaskRepository
) : TaskStatesRepository {
    override fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        return taskStatesDataSource.getAllTasksStates(includeDeleted)
            .filter { it.projectId == projectId }
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map { it.toTaskState() }
    }

    override fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        return taskStatesDataSource.getAllTasksStates(includeDeleted)
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == stateId }?.toTaskState()
            ?: throw TaskStateNotFoundException()
    }

    override fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        taskStatesDataSource.addNewTaskState(
            taskState.toTaskStateDto(projectId)
        )
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        taskStatesDataSource.editTaskStateTitle(stateId, newTitle)
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        taskStatesDataSource.editTaskStateDescription(stateId, newDescription)
    }

    override fun deleteTaskState(stateId: UUID) {
        taskStatesDataSource.deleteTaskState(stateId)
        tasksRepository.getTasksByTaskStateID(stateId, false)
            .map(Task::id)
            .forEach(tasksRepository::deleteTask)
    }
}