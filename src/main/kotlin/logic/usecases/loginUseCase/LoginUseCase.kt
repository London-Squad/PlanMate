package logic.usecases.loginUseCase

import logic.entities.User
import logic.repositories.AuthenticationRepository

class LoginUseCase(private val authRepository: AuthenticationRepository) {
    fun login(username: String, password: String): User? {

        return authRepository.login(username, password)
    }
}