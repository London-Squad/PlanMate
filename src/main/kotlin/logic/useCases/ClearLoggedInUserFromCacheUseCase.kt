package logic.useCases

import logic.repositories.AuthenticationRepository

class ClearLoggedInUserFromCacheUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun clearLoggedInUserFromCache() {
        authenticationRepository.clearLoggedInUserFromCache()
    }
}