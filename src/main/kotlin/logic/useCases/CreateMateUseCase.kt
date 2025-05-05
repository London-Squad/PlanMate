package logic.useCases

import logic.entities.User
import logic.exceptions.*
import logic.repositories.CacheDataRepository
import logic.repositories.MateRepository
import logic.validation.CredentialValidator

class CreateMateUseCase(
    private val cacheDataRepository: CacheDataRepository,
    private val mateRepository: MateRepository,
    private val credentialValidator: CredentialValidator
) {
    fun createMate(username: String, password: String) {
        val loggedInUser = try {
            cacheDataRepository.getLoggedInUser()
        } catch (e: Exception) {
            throw UserNotFoundException()
        }

        if (loggedInUser.type != User.Type.ADMIN) {
            throw UnauthorizedAccessException()
        }

        credentialValidator.takeIfValidNameOrThrowException(username)
        credentialValidator.takeIfValidPasswordOrThrowException(password)

        mateRepository.getMates()
            .any { it.userName == username }
            .takeIf { it }?.let {
                throw UsernameTakenException()
            }
        val registered = try {
            mateRepository.addMate(username, password)
        } catch (e: UserAlreadyExistException) {
            throw UsernameTakenException()
        } catch (e: Exception) {
            throw RegistrationFailedException()
        }

        if (!registered) {
            throw RegistrationFailedException()
        }

    }


}