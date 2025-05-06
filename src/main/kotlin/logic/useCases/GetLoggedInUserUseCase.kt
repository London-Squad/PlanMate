package logic.useCases

import logic.entities.User
import logic.repositories.AuthenticationRepository

class GetLoggedInUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun getLoggedInUser(): User {
        return authenticationRepository.getLoggedInUser()
    }
}