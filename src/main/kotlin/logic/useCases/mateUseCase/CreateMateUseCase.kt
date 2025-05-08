package logic.useCases.mateUseCase

import logic.repositories.UserRepository
import logic.validation.CredentialValidator

class CreateMateUseCase(
    private val userRepository: UserRepository,
    private val credentialValidator: CredentialValidator
) {
    fun createMate(username: String, password: String) {
        credentialValidator.validateUserName(username)
        credentialValidator.validatePassword(password)

        userRepository.addMate(username, password)
    }
}