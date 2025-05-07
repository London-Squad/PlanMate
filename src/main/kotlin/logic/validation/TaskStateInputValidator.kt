package logic.validation

class TaskStateInputValidator {

    fun isValidTaskStateTitle(input: String): Boolean =
        input.isNotBlank() && input.length <= MAX_TASK_STATE_TITLE_LENGTH

    fun isValidTaskStateDescription(input: String): Boolean = input.length <= MAX_TASK_STATE_DESCRIPTION_LENGTH

    companion object {
        private const val MAX_TASK_STATE_TITLE_LENGTH = 15
        private const val MAX_TASK_STATE_DESCRIPTION_LENGTH = 100
        const val INVALID_TITLE_MESSAGE =
            "Title cannot be empty, and max Title length is $MAX_TASK_STATE_TITLE_LENGTH"
        const val INVALID_DESCRIPTION_MESSAGE =
            "Max description length is $MAX_TASK_STATE_DESCRIPTION_LENGTH"
    }
}