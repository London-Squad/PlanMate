package logic.useCases

import logic.entities.State
import logic.entities.Task
import logic.repositories.TaskRepository
import java.util.UUID

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
) {

    fun addNewTask(task: Task, projectID: UUID) {
        taskRepository.addNewTask(task, projectID)
    }

    fun editTaskTitle(taskID: UUID, newTitle: String) {
        taskRepository.editTaskTitle(taskID, newTitle)
    }

    fun editTaskDescription(taskID: UUID, newDescription: String) {
        taskRepository.editTaskDescription(taskID, newDescription)
    }

    fun editTaskState(taskID: UUID, newState: State) {
        taskRepository.editTaskState(taskID, newState)
    }

    fun deleteTask(taskID: UUID) {
        taskRepository.deleteTask(taskID)
    }

}