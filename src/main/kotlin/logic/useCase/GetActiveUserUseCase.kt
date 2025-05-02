package logic.useCase

import logic.entities.User
import logic.repositories.CacheDataRepository

class GetActiveUserUseCase(
    private val cacheDataRepository: CacheDataRepository
) {
    operator fun invoke(): User? {
        return cacheDataRepository.getLoggedInUser()
    }
}