package logic.useCases

import logic.repositories.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(): Boolean {
        if (!authenticationRepository.logout()) return false
        authenticationRepository.clearLoggedInUserFromCache()
        return true
    }
}