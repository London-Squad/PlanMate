package data.dataSource

import logic.entities.State
import logic.entities.Task
import logic.repositories.StatesRepository
import logic.repositories.TaskRepository
import java.io.File
import java.util.*

class CsvTasksDataSource(
    private val file: File,
    private val statesRepository: StatesRepository
) : TaskRepository {

    init {
        val directory = file.parentFile
        if (!directory.exists()) {
            directory.mkdir()
        }

        if (!file.exists()) {
            file.createNewFile()
            file.writeText("id,title,description,stateID,projectID,\n")
        }
    }


    private fun getAllTasksPairedWithProjectID(): List<Pair<UUID, Task>> {
        return file.readLines().drop(1).map { line ->
            val (id, title, description, stateId, projectId) = line.split(",", limit = 5)
            Pair(
                UUID.fromString(projectId), Task(
                    id = UUID.fromString(id),
                    title = title,
                    description = description,
                    state = statesRepository.getStateById(UUID.fromString(stateId)) ?: State.NoState
                )
            )
        }
    }

    override fun getAllTasksByProjectID(projectId: UUID): List<Task> {
        return getAllTasksPairedWithProjectID()
            .filter { it.first == projectId }
            .map { it.second }
    }

    override fun addNewTask(task: Task, projectId: UUID) {
        file.appendText("${task.id},${task.title},${task.description},${task.state.id},${projectId}\n")
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        val tasksPairedWithProjectID = getAllTasksPairedWithProjectID()
            .map { (projectID, task) ->
                if (task.id == taskId) Pair(projectID, task.copy(title = newTitle))
                else Pair(projectID, task)
            }
        writeTasksToFile(tasksPairedWithProjectID)
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        val tasksPairedWithProjectID = getAllTasksPairedWithProjectID()
            .map { (projectID, task) ->
                if (task.id == taskId) Pair(projectID, task.copy(description = newDescription))
                else Pair(projectID, task)
            }
        writeTasksToFile(tasksPairedWithProjectID)
    }

    override fun editTaskState(taskId: UUID, newState: State) {
        val tasksPairedWithProjectID = getAllTasksPairedWithProjectID()
            .map { (projectID, task) ->
                if (task.id == taskId) Pair(projectID, task.copy(state = newState))
                else Pair(projectID, task)
            }
        writeTasksToFile(tasksPairedWithProjectID)
    }

    override fun deleteTask(taskId: UUID) {
        val tasksPairedWithProjectID = getAllTasksPairedWithProjectID()
            .filter { it.second.id != taskId }
        writeTasksToFile(tasksPairedWithProjectID)
    }

    private fun writeTasksToFile(tasksPairedWithProjectID: List<Pair<UUID, Task>>) {
        file.writeText("id,title,description,stateID,projectID,\n")
        tasksPairedWithProjectID.forEach { (projectID, task) ->
            file.appendText("${task.id},${task.title},${task.description},${task.state.id},${projectID}\n")
        }
    }
}