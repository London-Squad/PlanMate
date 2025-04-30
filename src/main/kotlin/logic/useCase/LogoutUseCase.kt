package logic.useCase

import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val cacheDataRepository: CacheDataRepository
) {
    operator fun invoke() {
        authenticationRepository.logout()
        cacheDataRepository.clearLoggedInUserFromCatch()
    }
}