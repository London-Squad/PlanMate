package data.repositories.dtoMappers.user

import data.dto.UserCsvDto

class UserCsvDtoMapper : UserDtoMapper<UserCsvDto> {

    override fun mapToUser(userDto: UserCsvDto) = userDto.toUser()

    override fun mapToCachedUser(userDto: UserCsvDto) = userDto

}