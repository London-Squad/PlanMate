package logic.useCases

import logic.repositories.AuthenticationRepository
import logic.validation.CredentialValidator

class SignupUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialValidator: CredentialValidator
) {
    operator fun invoke(userName: String, password: String): Boolean {
        credentialValidator.takeIfValidNameOrThrowException(userName)
        credentialValidator.takeIfValidPasswordOrThrowException(password)
        return authenticationRepository.addMate(userName, password)
    }
}