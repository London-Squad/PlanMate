package logic.useCases

import logic.entities.User
import logic.exceptions.*
import logic.exceptions.authenticationExceptions.AuthenticationException
import logic.exceptions.authenticationExceptions.RegistrationFailedException
import logic.exceptions.authenticationExceptions.UnauthorizedAccessException
import logic.exceptions.authenticationExceptions.UseNameAlreadyExistException
import logic.exceptions.notFoundExecption.UserNotFoundException
import logic.repositories.AuthenticationRepository
import logic.validation.CredentialValidator

class CreateMateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialValidator: CredentialValidator
) {
    fun createMate(username: String, password: String) {
        credentialValidator.validateUserName(username)
        credentialValidator.validatePassword(password)

        authenticationRepository.getMates()
            .any { it.userName == username }
            .takeIf { it }?.let {
                throw UseNameAlreadyExistException()
            }

        authenticationRepository.addMate(username, password)

    }


}