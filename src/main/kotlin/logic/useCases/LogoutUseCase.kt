package logic.useCases

import logic.repositories.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke() {
        authenticationRepository.logout()
    }

}