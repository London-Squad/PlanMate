package logic.useCases

import logic.entities.User
import logic.repositories.UserRepository

class GetUsersUseCase(
    private val usersRepository: UserRepository
) {
    fun getUsers(): List<User> {
        return usersRepository.getMates() + usersRepository.getAdmin()
    }
}