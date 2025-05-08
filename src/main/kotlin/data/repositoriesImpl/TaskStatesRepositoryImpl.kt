package data.repositoriesImpl

import data.csvDataSource.dtoMappers.toTaskState
import data.csvDataSource.dtoMappers.toTaskStateDto
import data.dataSources.TaskStatesDataSource
import data.dataSources.TasksDataSource
import logic.entities.TaskState
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TaskStatesRepository
import java.util.*

class TaskStatesRepositoryImpl(
    private val taskStatesDataSource: TaskStatesDataSource,
    private val tasksDataSource: TasksDataSource
) : TaskStatesRepository {
    override fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        return taskStatesDataSource.getAllTasksStates()
            .filter { it.projectId == projectId }
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map { it.toTaskState() }
    }

    override fun getDefaultTaskStates(projectId: UUID): List<TaskState> {
        return taskStatesDataSource.createDefaultTaskStatesForProject(projectId).map { it.toTaskState() }
    }

    override fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        return taskStatesDataSource.getAllTasksStates()
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
        tasksDataSource.getAllTasks().forEach { taskDto ->
            if (taskDto.stateId == stateId) tasksDataSource.deleteTask(taskDto.id)
        }
    }
}