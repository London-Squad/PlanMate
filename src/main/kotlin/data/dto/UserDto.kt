package data.dto

data class UserDto(
    val id: String,
    val userName: String,
    val hashedPassword: String,
    val type: String,
    val isDeleted: Boolean
)