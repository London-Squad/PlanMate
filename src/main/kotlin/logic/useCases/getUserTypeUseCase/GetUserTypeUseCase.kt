package logic.useCases.getUserTypeUseCase

import logic.entities.User
import logic.repositories.AuthenticationRepository

class GetUserTypeUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun getUserType(): User.Type? {
        return authenticationRepository.getActiveUser()?.type
    }
}