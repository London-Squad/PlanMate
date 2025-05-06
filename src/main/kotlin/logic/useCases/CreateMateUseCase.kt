package logic.useCases

import logic.repositories.AuthenticationRepository
import logic.validation.CredentialValidator

class CreateMateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialValidator: CredentialValidator
) {
    fun createMate(username: String, password: String) {
        credentialValidator.validateUserName(username)
        credentialValidator.validatePassword(password)

        authenticationRepository.addMate(username, password)
    }
}