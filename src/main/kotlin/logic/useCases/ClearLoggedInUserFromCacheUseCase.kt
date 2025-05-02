package logic.useCases

import logic.repositories.CacheDataRepository

class ClearLoggedInUserFromCacheUseCase(
    private val cacheDataRepository: CacheDataRepository
) {
    fun clearLoggedInUserFromCache() {
        cacheDataRepository.clearLoggedInUserFromCache()
    }
}