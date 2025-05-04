package logic.validation

class UserInputValidator {

    fun isValidTitle(input: String): Boolean = input.isNotBlank() && input.length <= MAX_TITLE_LENGTH
    fun isValidDescription(input: String): Boolean = input.length <= MAX_DESCRIPTION_LENGTH

    companion object {
        private const val MAX_TITLE_LENGTH = 30
        private const val MAX_DESCRIPTION_LENGTH = 250
        const val INVALID_TITLE_MESSAGE =
            "Title cannot be empty, and max Title length is $MAX_TITLE_LENGTH"
        const val INVALID_DESCRIPTION_MESSAGE =
            "Max description length is $MAX_DESCRIPTION_LENGTH"
    }
}