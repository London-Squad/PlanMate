package logic.useCases

import logic.entities.User
import logic.repositories.AuthenticationRepository

class SetLoggedInUserUseCase(
    private val authenticationRepository: AuthenticationRepository

) {
    fun setLoggedInUser(user: User) {
        authenticationRepository.setLoggedInUser(user = user)
    }
}