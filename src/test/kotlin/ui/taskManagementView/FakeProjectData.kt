//package ui.taskManagementView
//
//import logic.entities.Project
//import logic.entities.Task
//import logic.entities.TaskState
//import java.util.*
//
//object FakeProjectData {
//    val taskStatesLists = listOf(
//        TaskState(UUID.randomUUID(), "Todo", "Todo description"),
//        TaskState(UUID.randomUUID(), "Done", "Done description"),
//        TaskState(UUID.randomUUID(), "In-progress", "In-progress description"),
//    )
//
//    val tasks = listOf(
//        Task(UUID.randomUUID(), "Fake Task 1", description = "description1", taskStatesLists[0]),
//        Task(UUID.randomUUID(), "Fake Task 2", description = "description2", taskStatesLists[1]),
//        Task(UUID.randomUUID(), "Fake Task 3", description = "description3", taskStatesLists[2]),
//    )
//    val project = Project(
//        UUID.randomUUID(), "Fake Project", "description",
//        tasks = tasks, tasksStates = taskStatesLists
//    )
//}