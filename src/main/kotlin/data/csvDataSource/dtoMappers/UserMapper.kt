package data.csvDataSource.dtoMappers

import data.dto.UserDto
import logic.entities.User

fun UserDto.toUser(): User {
    return User(
        id = this.id,
        userName = this.userName,
        type = User.Type.valueOf(this.type.uppercase())
    )
}

fun User.toUserDto(hashedPassword: String, isDeleted: Boolean = false): UserDto {
    return UserDto(
        id = this.id,
        userName = this.userName,
        hashedPassword = hashedPassword,
        type = this.type.name,
        isDeleted = isDeleted
    )
}