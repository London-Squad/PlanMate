package logic.useCases

import logic.repositories.MateRepository

class ChangePasswordUseCase(
    private val mateRepository: MateRepository
) {
    operator fun invoke(username: String, oldPassword: String, newPassword: String): Boolean {
        return mateRepository.changeMatePassword(username, oldPassword, newPassword)
    }
}