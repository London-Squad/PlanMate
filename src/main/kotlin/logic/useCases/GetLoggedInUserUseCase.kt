package logic.useCases

import logic.entities.User
import logic.repositories.AuthenticationRepository

class GetLoggedInUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun getLoggedInUser(): User {
        return authenticationRepository.getLoggedInUser()
    }
}