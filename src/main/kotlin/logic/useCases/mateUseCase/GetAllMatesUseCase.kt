package logic.useCases.mateUseCase

import logic.entities.User
import logic.repositories.UserRepository

class GetAllMatesUseCase(
    private val userRepository: UserRepository
) {
    suspend fun getAllMates(): List<User> {
        return userRepository.getMates()
    }
}