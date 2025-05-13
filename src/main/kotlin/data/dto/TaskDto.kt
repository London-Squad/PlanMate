package data.dto

data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val stateId: String,
    val projectId: String,
    val isDeleted: Boolean
)