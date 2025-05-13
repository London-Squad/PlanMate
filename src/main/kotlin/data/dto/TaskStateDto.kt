package data.dto

data class TaskStateDto(
    val id: String,
    val title: String,
    val description: String,
    val projectId: String,
    val isDeleted: Boolean
)