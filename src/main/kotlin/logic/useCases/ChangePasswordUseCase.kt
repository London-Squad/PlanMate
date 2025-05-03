package logic.useCases

import logic.repositories.AuthenticationRepository
import logic.validation.CredentialValidator

class ChangePasswordUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialValidator: CredentialValidator
) {
    operator fun invoke(username: String, oldPassword: String, newPassword: String): Boolean {
        credentialValidator.takeIfValidNameOrThrowException(username)
        credentialValidator.takeIfValidPasswordOrThrowException(oldPassword)
        credentialValidator.takeIfValidPasswordOrThrowException(newPassword)
        return authenticationRepository.changePassword(username, oldPassword, newPassword)
    }
}