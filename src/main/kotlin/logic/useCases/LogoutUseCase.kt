package logic.useCases

import logic.repositories.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke() {
        authenticationRepository.logout()
    }

}