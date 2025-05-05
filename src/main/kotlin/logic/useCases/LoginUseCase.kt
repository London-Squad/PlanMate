package logic.useCases

import logic.entities.User
import logic.repositories.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(userName: String, password: String): User {
        return authenticationRepository.login(userName, password)
    }
}