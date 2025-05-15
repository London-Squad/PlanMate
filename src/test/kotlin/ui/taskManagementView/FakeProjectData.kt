package ui.taskManagementView

import logic.entities.Project
import logic.entities.Task
import logic.entities.TaskState
import java.util.*

object FakeProjectData {
    val project = Project(
        UUID.randomUUID(), "Fake Project", "description",
    )
    val taskStatesLists = listOf(
        TaskState(UUID.randomUUID(), "Todo", "Todo description", project.id),
        TaskState(UUID.randomUUID(), "Done", "Done description", project.id),
        TaskState(UUID.randomUUID(), "In-progress", "In-progress description", project.id),
    )

    val tasks = listOf(
        Task(UUID.randomUUID(), "Fake Task 1", description = "description1", taskStatesLists[0].id),
        Task(UUID.randomUUID(), "Fake Task 2", description = "description2", taskStatesLists[1].id),
        Task(UUID.randomUUID(), "Fake Task 3", description = "description3", taskStatesLists[2].id),
    )
}