package logic.validation

class TaskInputValidator {

    fun isValidTaskTitle(input: String): Boolean = input.isNotBlank() && input.length <= MAX_TASK_TITLE_LENGTH
    fun isValidTaskDescription(input: String): Boolean = input.length <= MAX_TASK_DESCRIPTION_LENGTH

    companion object {
        private const val MAX_TASK_TITLE_LENGTH = 30
        private const val MAX_TASK_DESCRIPTION_LENGTH = 150
        const val INVALID_TITLE_MESSAGE =
            "Title cannot be empty, and max Title length is $MAX_TASK_TITLE_LENGTH"
        const val INVALID_DESCRIPTION_MESSAGE =
            "Max description length is $MAX_TASK_DESCRIPTION_LENGTH"
    }
}