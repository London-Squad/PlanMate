package data.dto

data class LogDto(
    val id: String,
    val userId: String,
    val time: String,
    val action: String,
    val planEntityId: String,
    val planEntityProperty: String,
    val oldValue: String,
    val newValue: String
)