package logic.usecases.login_usecase

import logic.repositories.AuthenticationRepository

class LoginUseCase(private val authRepository: AuthenticationRepository) {
    fun login() {}
}