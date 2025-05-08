package ui.cliPrintersAndReaders

import logic.validation.ProjectInputValidator

class ProjectInputReader(
    private val cliReader: CLIReader,
    private val projectInputValidator: ProjectInputValidator,
    ) {

    fun getValidProjectTitle(): String {
        return cliReader.getValidUserInput(
            message = "Enter title: ",
            invalidInputMessage = "Title cannot be empty, and max Title length is ${ProjectInputValidator.MAX_PROJECT_TITLE_LENGTH}",
            isValidInput = projectInputValidator::isValidProjectTitle
        )
    }

    fun getValidProjectDescription(): String {
        return cliReader.getValidUserInput(
            message = "Enter description: ",
            invalidInputMessage = "Max description length is ${ProjectInputValidator.MAX_PROJECT_DESCRIPTION_LENGTH}",
            isValidInput = projectInputValidator::isValidProjectDescription
        )
    }
}