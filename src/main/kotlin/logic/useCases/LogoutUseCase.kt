package logic.useCases

import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val cacheDataRepository: CacheDataRepository
) {
    operator fun invoke(): Boolean {
        if (!authenticationRepository.logout()) return false
        cacheDataRepository.clearLoggedInUserFromCatch()
        return true
    }
}