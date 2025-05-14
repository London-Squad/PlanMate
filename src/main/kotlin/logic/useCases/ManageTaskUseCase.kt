package logic.useCases

import logic.entities.Log.EntityType
import logic.entities.Task
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import java.util.UUID

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
        createLogUseCase.logEntityCreation(newTask.id, EntityType.TASK)
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
        createLogUseCase.logEntityTitleEdition(oldTask.id,  EntityType.TASK,oldTask.title, newTitle)
    }

    suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskDescription(taskId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(oldTask.id,  EntityType.TASK,oldTask.description, newDescription)
    }

    suspend fun editTaskState(taskId: UUID, newTaskStateId: UUID) {
        val oldTask = taskRepository.getTaskByID(taskId)

        val oldTaskState = taskStatesRepository.getTaskStateById(oldTask.taskStateId)
        val newTaskState = taskStatesRepository.getTaskStateById(newTaskStateId)

        taskRepository.editTaskState(taskId, newTaskStateId)
        createLogUseCase.logTaskStateEdition(taskId, oldTaskState.title, newTaskState.title)
    }

    suspend fun deleteTask(taskId: UUID) {
        taskRepository.deleteTask(taskId)
        createLogUseCase.logEntityDeletion(taskId, EntityType.TASK)
    }
}