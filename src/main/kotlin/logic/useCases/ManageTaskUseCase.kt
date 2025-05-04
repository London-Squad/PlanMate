package logic.useCases

import logic.entities.*
import logic.repositories.CacheDataRepository
import logic.repositories.LogsRepository
import logic.repositories.TaskRepository
import java.util.UUID

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val cacheDataRepository: CacheDataRepository,
    private val logsRepository: LogsRepository,
) {

    fun editTaskTitle(taskID: UUID, newTitle: String) {
            val task = taskRepository.getTaskByID(taskID)

            taskRepository.editTaskTitle(taskID, newTitle)
            logsRepository.addLog(
                Log(
                    user = cacheDataRepository.getLoggedInUser(),
                    action = Edit(
                        entity = task,
                        property = "title",
                        oldValue = task.title,
                        newValue = newTitle
                    )
                )
            )
    }

    fun editTaskDescription(taskID: UUID, newDescription: String) {
        val task = taskRepository.getTaskByID(taskID)

        taskRepository.editTaskDescription(taskID, newDescription)
        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                action = Edit(
                    entity = task,
                    property = "description",
                    oldValue = task.description,
                    newValue = newDescription
                )
            )
        )
    }

    fun editTaskState(taskID: UUID, newState: State) {
        val task = taskRepository.getTaskByID(taskID)

        taskRepository.editTaskState(taskID, newState)
        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                action = Edit(
                    entity = task,
                    property = "state",
                    oldValue = task.state.title,
                    newValue = newState.title
                )
            )
        )
    }

    fun deleteTask(taskID: UUID) {
        val task = taskRepository.getTaskByID(taskID)

        taskRepository.deleteTask(taskID)
        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                action = Delete(
                    entity = task,
                )
            )
        )
    }

}