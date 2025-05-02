package logic.useCases

import logic.entities.User
import logic.exceptions.AuthenticationException
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.validation.takeIfValidNameOrThrowException
import logic.validation.takeIfValidPasswordOrThrowException

class CreateMateUseCase(
    private val cacheDataRepository: CacheDataRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    fun createMate(username: String, password: String) {
        val loggedInUser = try {
            cacheDataRepository.getLoggedInUser()
        } catch (e: Exception) {
            throw AuthenticationException.UserNotFoundException()
        }

        if (loggedInUser.type != User.Type.ADMIN) {
            throw AuthenticationException.UnauthorizedAccessException()
        }

        username.takeIfValidNameOrThrowException()
        password.takeIfValidPasswordOrThrowException()

        authenticationRepository.getMates()
            .any { it.userName == username }
            .takeIf { it }?.let {
                throw AuthenticationException.UsernameTakenException()
            }
        val registered = try {
            authenticationRepository.register(username, password)
        } catch (e: AuthenticationException.UserAlreadyExistException) {
            throw AuthenticationException.UsernameTakenException()
        } catch (e: Exception) {
            throw AuthenticationException.RegistrationFailedException()
        }

        if (!registered) {
            throw AuthenticationException.RegistrationFailedException()
        }

    }


}