package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.TaskRepository
import logic.repositories.LogsRepository
import java.time.LocalDateTime
import java.util.UUID

class ManageTaskUsecase(
    private val taskRepository: TaskRepository,
) {

    fun editTaskTitle(taskID: UUID, newTitle: String) {
        taskRepository.editTaskTitle(taskID, newTitle)
    }

    fun editTaskDescription(taskID: UUID, newDescription: String) {
        taskRepository.editTaskDescription(taskID, newDescription)
    }

    fun editTaskState(taskID: UUID, newStateID: UUID) {
        taskRepository.editTaskState(taskID, newStateID)
    }

    fun deleteTask(taskID: UUID) {
        taskRepository.deleteTask(taskID)
    }

}