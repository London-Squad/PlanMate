package logic.useCases

import logic.entities.User
import logic.repositories.AuthenticationRepository
import logic.validation.CredentialValidator

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialValidator: CredentialValidator
) {
    operator fun invoke(userName: String, password: String): User {
        credentialValidator.takeIfValidNameOrThrowException(userName)
        credentialValidator.takeIfValidPasswordOrThrowException(password)
        val user = authenticationRepository.login(userName, password)
        authenticationRepository.setLoggedInUser(user)
        return user
    }
}