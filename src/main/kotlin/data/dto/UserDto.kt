package data.dto

import java.util.UUID

data class UserDto(
    val id: UUID,
    val userName: String,
    val hashedPassword: String,
    val type: String,
    val isDeleted: Boolean
)