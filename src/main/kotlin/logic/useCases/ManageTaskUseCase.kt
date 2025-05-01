package logic.useCases

import logic.repositories.TaskRepository
import java.util.UUID

class ManageTaskUseCase(
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