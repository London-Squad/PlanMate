package logic.useCases

import logic.entities.User
import logic.exceptions.*
import logic.repositories.AuthenticationRepository
import logic.validation.CredentialValidator

class CreateMateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialValidator: CredentialValidator
) {
    fun createMate(username: String, password: String) {
        val loggedInUser = try {
            authenticationRepository.getLoggedInUser()
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
            authenticationRepository.addMate(username, password)
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