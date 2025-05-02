package logic.usecases

import logic.entities.User
import logic.exception.AuthenticationException
import logic.repositories.AuthenticationRepository
import logic.validation.takeIfValidNameOrThrowException
import logic.validation.takeIfValidPasswordOrThrowException

class CreateMateUseCase(private val authenticationRepository: AuthenticationRepository) {
    fun createMate(username: String, password: String): User {
        username.takeIfValidNameOrThrowException()
        password.takeIfValidPasswordOrThrowException ()

        val isUsernameTaken = authenticationRepository.getMates().any { it.userName == username }
        if (isUsernameTaken) {
            throw AuthenticationException.UsernameTakenException()
        }

        return try {
            authenticationRepository.register(username, password)
        } catch (e: AuthenticationException.UsernameTakenException) {
            throw AuthenticationException.UsernameTakenException()
        }
    }

}