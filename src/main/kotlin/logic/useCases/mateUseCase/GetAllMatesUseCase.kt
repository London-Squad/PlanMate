package logic.useCases

import logic.entities.User
import logic.repositories.AuthenticationRepository

class GetAllMatesUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun getAllMates(): List<User> {
        return authenticationRepository.getMates()
    }
}