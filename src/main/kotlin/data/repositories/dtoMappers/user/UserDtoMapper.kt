package data.repositories.dtoMappers.user

import data.dto.UserCsvDto
import logic.entities.User

interface UserDtoMapper<DTO> {

    fun mapToUser(userDto: DTO): User

    fun mapToCachedUser(userDto: DTO): UserCsvDto

}