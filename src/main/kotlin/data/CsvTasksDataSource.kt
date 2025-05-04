package data

import data.csvHandler.CsvFileHandler
import data.parser.TaskParseResult
import data.parser.TaskParser
import logic.entities.State
import logic.entities.Task
import logic.repositories.TaskRepository
import java.util.UUID

class CsvTasksDataSource(
    private val fileHandler: CsvFileHandler,
    private val taskParser: TaskParser
) : TaskRepository {

    init {
        fileHandler.writeHeader("id,title,description,stateID,projectID")
    }

    private fun getAllTasksPaired(): List<Pair<UUID, Task>> =
        fileHandler.readLines().drop(1).mapNotNull { line ->
            when (val result = taskParser.parseTaskLine(line)) {
                is TaskParseResult.Success -> Pair(result.projectId, result.task)
                is TaskParseResult.Failure -> null
            }
        }

    private fun updateTasks(transform: (List<Pair<UUID, Task>>) -> List<Pair<UUID, Task>>) {
        val allLines = fileHandler.readLines()
        val header = allLines.firstOrNull() ?: "id,title,description,stateID,projectID"
        val tasks = getAllTasksPaired()
        val updatedTasks = transform(tasks)

        // Always preserve the header when rewriting
        fileHandler.rewriteLines(
            listOf(header) +
                    updatedTasks.map { taskParser.formatTaskLine(it.first, it.second) }
        )
    }

    override fun getAllTasksByProjectID(projectId: UUID): List<Task> =
        getAllTasksPaired().filter { it.first == projectId }.map { it.second }

    override fun getTaskByID(taskId: UUID): Task? =
        getAllTasksPaired().find { it.second.id == taskId }?.second

    override fun addNewTask(task: Task, projectId: UUID) {
        fileHandler.appendLine(taskParser.formatTaskLine(projectId, task))
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) =
        updateTasks { tasks -> tasks.map { if (it.second.id == taskId) it.copy(second = it.second.copy(title = newTitle)) else it } }

    override fun editTaskDescription(taskId: UUID, newDescription: String) =
        updateTasks { tasks -> tasks.map { if (it.second.id == taskId) it.copy(second = it.second.copy(description = newDescription)) else it } }

    override fun editTaskState(taskId: UUID, newState: State) =
        updateTasks { tasks -> tasks.map { if (it.second.id == taskId) it.copy(second = it.second.copy(state = newState)) else it } }

    override fun deleteTask(taskId: UUID) =
        updateTasks { tasks -> tasks.filter { it.second.id != taskId } }
}