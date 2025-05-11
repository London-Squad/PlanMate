package logic.useCases

import logic.entities.User
import logic.repositories.UserRepository
import java.util.*

class GetUserByIdUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: UUID): User {
        return userRepository.getUserById(userId)
    }

}