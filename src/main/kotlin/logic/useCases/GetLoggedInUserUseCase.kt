package logic.useCases

import logic.entities.User
import logic.repositories.CacheDataRepository

class GetLoggedInUserUseCase(
    private val cacheDataRepository: CacheDataRepository
) {
    fun getLoggedInUser(): User {
        return cacheDataRepository.getLoggedInUser()
    }
}