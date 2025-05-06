package data.mongoDBDataSource

import data.dto.TaskDto
import java.util.UUID

fun main() {
    val db = DatabaseConnection.getTasksCollection()
    val repository = MongoDBTasksDataSource(db)
    addTaskUI(repository)

    val tasks = repository.getAllTasks()
    tasks.forEach { println(it) }

    DatabaseConnection.close()
}

private fun addTaskUI(repository: MongoDBTasksDataSource) {
    println("Enter task name:")
    val name = readln()
    println("Enter task description:")
    val description = readln()
    val task = TaskDto(
        id = UUID.randomUUID(),
        title = name,
        description = description,
        stateId = UUID.randomUUID(),
        projectId = UUID.randomUUID(),
        isDeleted = false
    )
    repository.addNewTask(task)
    println("Task saved!")
}