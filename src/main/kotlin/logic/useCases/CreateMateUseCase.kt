package logic.useCases

import logic.entities.User
import logic.exceptions.*
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.validation.CredentialValidator

class CreateMateUseCase(
    private val cacheDataRepository: CacheDataRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val credentialValidator: CredentialValidator
) {
    fun createMate(username: String, password: String) {
        val loggedInUser = try {
            cacheDataRepository.getLoggedInUser()
        } catch (e: Exception) {
            throw UserNotFoundException()
        }

        if (loggedInUser.type != User.Type.ADMIN) {
            throw UnauthorizedAccessException()
        }

        credentialValidator.takeIfValidNameOrThrowException(username)
        credentialValidator.takeIfValidPasswordOrThrowException(password)

        authenticationRepository.getMates()
            .any { it.userName == username }
            .takeIf { it }?.let {
                throw UsernameTakenException()
            }
        val registered = try {
            authenticationRepository.register(username, password)
        } catch (e: UserAlreadyExistException) {
            throw UsernameTakenException()
        } catch (e: Exception) {
            throw RegistrationFailedException()
        }

        if (!registered) {
            throw RegistrationFailedException()
        }

    }


}