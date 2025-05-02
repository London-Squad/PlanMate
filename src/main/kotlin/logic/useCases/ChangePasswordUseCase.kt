package logic.useCases

import logic.repositories.AuthenticationRepository
import logic.validation.takeIfValidPasswordOrThrowException
import logic.validation.takeIfValidNameOrThrowException

class ChangePasswordUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, oldPassword: String, newPassword: String): Boolean {
        username.takeIfValidNameOrThrowException()
        oldPassword.takeIfValidPasswordOrThrowException()
        newPassword.takeIfValidPasswordOrThrowException()
        return authenticationRepository.changePassword(username, oldPassword, newPassword)
    }
}