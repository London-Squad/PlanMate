
import logic.entities.User
import logic.repositories.UserRepository

class GetUsersUseCase(
    private val usersRepository: UserRepository
) {
    suspend fun getUsers(): List<User> {
        return usersRepository.getMates() + usersRepository.getAdmin()
    }
}