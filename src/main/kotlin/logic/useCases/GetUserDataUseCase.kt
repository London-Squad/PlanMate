
import logic.entities.User
import logic.repositories.UserRepository
import java.util.UUID

class GetUserDataUseCase(
    private val usersRepository: UserRepository
) {
    suspend fun getUsers(): List<User> {
        return usersRepository.getUsers()
    }
    suspend fun getUserById(userId: UUID): User {
        return usersRepository.getUserById(userId)
    }
}