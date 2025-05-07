package data.dataSources

import data.dto.TaskStateDto
import java.util.*

fun getDefaultTaskStates(projectId: UUID) = listOf(
    TaskStateDto(
        id = UUID.randomUUID(), title = "TODO", description = "TO DO TASKS",
        projectId = projectId,
        isDeleted = false
    ),
    TaskStateDto(
        id = UUID.randomUUID(), title = "InProgress", description = "INPROGRESS TASKS",
        projectId = projectId,
        isDeleted = false
    ),
    TaskStateDto(
        id = UUID.randomUUID(), title = "Done", description = "FINISHED TASKS",
        projectId = projectId,
        isDeleted = false
    )
)