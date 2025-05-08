package logic.validation

class TaskStateInputValidator {

    fun isValidTaskStateTitle(input: String): Boolean =
        input.isNotBlank() && input.length <= MAX_TASK_STATE_TITLE_LENGTH

    fun isValidTaskStateDescription(input: String): Boolean = input.length <= MAX_TASK_STATE_DESCRIPTION_LENGTH

    companion object {
        const val MAX_TASK_STATE_TITLE_LENGTH = 15
        const val MAX_TASK_STATE_DESCRIPTION_LENGTH = 100
    }
}