package logic.useCases

import logic.entities.User
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.validation.CredentialValidator

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val cacheDataRepository: CacheDataRepository,
    private val credentialValidator: CredentialValidator
) {
    operator fun invoke(userName: String, password: String): User {
        credentialValidator.takeIfValidNameOrThrowException(userName)
        credentialValidator.takeIfValidPasswordOrThrowException(password)
        val user = authenticationRepository.login(userName, password)
        cacheDataRepository.setLoggedInUser(user)
        return user
    }
}