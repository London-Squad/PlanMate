package logic.useCases

import logic.entities.Task
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import java.util.*

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createLogUseCase: CreateLogUseCase,
    private val taskStatesRepository: TaskStatesRepository
) {

    suspend fun getTaskByID(taskId: UUID): Task {
        return taskRepository.getTaskByID(taskId)
    }

    suspend fun addNewTask(title: String, description: String, projectId: UUID) {

        val newTask = buildNewTask(UUID.randomUUID(), title, description, projectId)

        taskRepository.addNewTask(newTask, projectId)
        createLogUseCase.logEntityCreation(newTask.id)
    }

    private suspend fun buildNewTask(
        id: UUID, title: String, description: String, projectId: UUID
    ): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            taskStateId = taskStatesRepository.getTaskStatesByProjectId(projectId).first().id
        )
    }

    suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskTitle(taskId, newTitle)
        createLogUseCase.logEntityTitleEdition(oldTask.id, oldTask.title, newTitle)
    }

    suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskDescription(taskId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(oldTask.id, oldTask.description, newDescription)
    }

    suspend fun editTaskState(taskId: UUID, newTaskStateId: UUID) {
        val newTaskState = taskStatesRepository.getTaskStateById(newTaskStateId)

        taskRepository.editTaskState(taskId, newTaskState)
        createLogUseCase.logTaskStateEdition(taskId, newTaskState.title, newTaskState.title)
    }

    suspend fun deleteTask(taskId: UUID) {
        taskRepository.deleteTask(taskId)
        createLogUseCase.logEntityDeletion(taskId)
    }
}