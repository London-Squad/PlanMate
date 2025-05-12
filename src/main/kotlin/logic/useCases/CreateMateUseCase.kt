package logic.useCases

import logic.entities.User
import logic.exceptions.UnauthorizedOperationException
import logic.repositories.AuthenticationRepository
import logic.repositories.UserRepository
import logic.validation.CredentialValidator

class CreateMateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
    private val credentialValidator: CredentialValidator
) {
    suspend fun createMate(username: String, password: String) {

        if (authenticationRepository.getLoggedInUser().type != User.Type.ADMIN) {
            throw UnauthorizedOperationException("Only admins can create mates.")
        }

        credentialValidator.validateUserName(username)
        credentialValidator.validatePassword(password)

        userRepository.addMate(username, password)
    }
}