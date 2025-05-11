package logic.useCases

import logic.entities.*
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import java.util.*
import kotlin.collections.List

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createLogUseCase: CreateLogUseCase,
    private val taskStatesRepository: TaskStatesRepository
) {

    fun getTaskByID(taskId: UUID): Task {
        return taskRepository.getTaskByID(taskId)
    }


    fun getTasksByProjectID(projectId: UUID): List<Task> {
        return taskRepository.getTasksByProjectID(projectId)
    }

    fun addNewTask(title: String, description: String, projectId: UUID) {

        val newTask = buildNewTask(UUID.randomUUID(), title, description, projectId)

        taskRepository.addNewTask(newTask, projectId)
        createLogUseCase.logEntityCreation(newTask.id)
    }

    private fun buildNewTask(
        id: UUID, title: String, description: String, projectId: UUID
    ): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            taskStateId = taskStatesRepository.getTaskStatesByProjectId(projectId).first().id
        )
    }

    fun editTaskTitle(taskId: UUID, newTitle: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskTitle(taskId, newTitle)
        createLogUseCase.logEntityTitleEdition(oldTask.id, oldTask.title, newTitle)
    }

    fun editTaskDescription(taskId: UUID, newDescription: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskDescription(taskId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(oldTask.id, oldTask.description, newDescription)
    }

    fun editTaskState(taskId: UUID, newTaskStateId: UUID) {
        val newTaskState = taskStatesRepository.getTaskStateById(newTaskStateId)

        taskRepository.editTaskState(taskId, newTaskState)
        createLogUseCase.logTaskStateEdition(taskId, newTaskState.title, newTaskState.title)
    }

    fun deleteTask(taskId: UUID) {
        taskRepository.deleteTask(taskId)
        createLogUseCase.logEntityDeletion(taskId)
    }
}