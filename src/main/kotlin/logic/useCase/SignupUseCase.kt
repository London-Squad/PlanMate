package logic.useCase

import logic.repositories.AuthenticationRepository
import logic.validation.takeIfValidPasswordOrThrowException
import logic.validation.takeIfValidNameOrThrowException

class SignupUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(userName: String, password: String): Boolean {
        userName.takeIfValidNameOrThrowException()
        password.takeIfValidPasswordOrThrowException()
        return authenticationRepository.register(userName, password)
    }
}