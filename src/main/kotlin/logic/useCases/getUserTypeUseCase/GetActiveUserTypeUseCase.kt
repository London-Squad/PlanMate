package logic.useCases.getUserTypeUseCase

import logic.entities.User
import logic.repositories.AuthenticationRepository

class GetActiveUserTypeUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun getActiveUserType(): User.Type? {
        return authenticationRepository.getActiveUser()?.type
    }
}