package data.repositoriesImpl

import data.dataSources.TasksStatesDataSource
import data.csvDataSource.DtoMapper
import data.dataSources.TasksDataSource
import logic.entities.TaskState
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TasksStatesRepository
import java.util.*

class TasksStatesRepositoryImpl(
    private val tasksStatesDataSource: TasksStatesDataSource,
    private val tasksDataSource: TasksDataSource,
    private val mapper: DtoMapper
) : TasksStatesRepository {
    override fun getTasksStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        return tasksStatesDataSource.getAllTasksStates()
            .filter { it.projectId == projectId }
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map(mapper::mapToTaskState)
    }

    override fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        return tasksStatesDataSource.getAllTasksStates()
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == stateId }
            ?.let(mapper::mapToTaskState)
            ?: throw TaskStateNotFoundException()
    }

    override fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        tasksStatesDataSource.addNewTaskState(
            mapper.mapToTaskStateDto(taskState, projectId)
        )
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        tasksStatesDataSource.editTaskStateTitle(stateId, newTitle)
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        tasksStatesDataSource.editTaskStateDescription(stateId, newDescription)
    }

    override fun deleteTaskState(stateId: UUID) {
        tasksStatesDataSource.deleteTaskState(stateId)
        tasksDataSource.getAllTasks().forEach { taskDto ->
            if (taskDto.stateId == stateId) tasksDataSource.deleteTask(taskDto.id)
        }
    }
}