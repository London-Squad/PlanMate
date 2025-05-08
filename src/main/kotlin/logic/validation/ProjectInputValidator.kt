package logic.validation

class ProjectInputValidator {

    fun isValidProjectTitle(input: String): Boolean = input.isNotBlank() && input.length <= MAX_PROJECT_TITLE_LENGTH
    fun isValidProjectDescription(input: String): Boolean = input.length <= MAX_PROJECT_DESCRIPTION_LENGTH

    companion object {
        const val MAX_PROJECT_TITLE_LENGTH = 30
        const val MAX_PROJECT_DESCRIPTION_LENGTH = 250
    }
}