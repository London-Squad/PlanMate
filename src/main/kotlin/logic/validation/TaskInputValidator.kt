package logic.validation

class TaskInputValidator {

    fun isValidTaskTitle(input: String): Boolean = input.isNotBlank() && input.length <= MAX_TASK_TITLE_LENGTH
    fun isValidTaskDescription(input: String): Boolean = input.length <= MAX_TASK_DESCRIPTION_LENGTH

    companion object {
        const val MAX_TASK_TITLE_LENGTH = 30
        const val MAX_TASK_DESCRIPTION_LENGTH = 150
    }
}