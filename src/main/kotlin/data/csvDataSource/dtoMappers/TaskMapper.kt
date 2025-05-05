package data.csvDataSource.dtoMappers

import data.entitiesData.TaskData
import logic.entities.State
import logic.entities.Task
import java.util.*

fun TaskData.mapToTask(taskState: State): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        state = taskState
    )
}

fun Task.mapToTaskData(task: Task, projectId: UUID, isDeleted: Boolean = false): TaskData {
    return TaskData(
        id = task.id,
        title = task.title,
        description = task.description,
        stateId = task.state.id,
        projectId = projectId,
        isDeleted = isDeleted
    )
}