package ui.utils

import logic.entities.Project

fun readUserInput(label: String): String? {
    println(label)
    return try {
        readLine()?.trim()?.takeIf { it.isNotBlank() }
    } catch (e: Exception) {
        println("Error reading input: ${e.message}")
        null
    }
}


fun readValidOption(label: String, maxOption: Int): String {
    val userInput = readUserInput(label) ?: return readValidOption(label, maxOption)
    val userInputInt = userInput.toIntOrNull()
    return if (userInputInt != null && userInputInt in 0..maxOption) {
        userInput
    } else {
        println("Invalid option. Please enter a number between 0 and $maxOption.")
        readValidOption(label, maxOption)
    }
}

fun readValidTitle(label: String, maxLength: Int): String {
    val userInput = readUserInput(label) ?: return readValidTitle(label, maxLength)
    return if (userInput.isNotBlank() && userInput.length <= maxLength) userInput
    else {
        println("Invalid title. Must be non-empty and at most $maxLength characters.")
        readValidTitle(label, maxLength)
    }
}

fun readValidDescription(label: String, maxLength: Int): String {
    val userInput = readUserInput(label) ?: return readValidDescription(label, maxLength)
    return if (userInput.isNotBlank() && userInput.length <= maxLength) userInput
    else {
        println("Invalid description. Must be non-empty and at most $maxLength characters.")
        readValidDescription(label, maxLength)
    }
}

fun readYesNoConfirmation(label: String): Boolean {
    return when (readUserInput(label)) {
        "y" -> true
        "n" -> false
        else -> {
            println("Invalid input. Please enter 'y' for yes or 'n' for no.")
            readYesNoConfirmation(label)
        }
    }
}

fun formatProjectDetails(project: Project): String {
    return """
      
        ╠══════════════════════╪════════════════════════════════╪══════════════════════════════════════╣
        ║ $project.title       │ $project.description           │ $project.id                          ║
        ╚══════════════════════╧════════════════════════════════╧══════════════════════════════════════╝
    """
}

fun tableHeader(): String {
    return """
        ╔══════════════════════╤════════════════════════════════╤══════════════════════════════════════╗
        ║ Title                │ Description                    │ ID                                   ║
        ╠══════════════════════╪════════════════════════════════╪══════════════════════════════════════╣
    """
}
fun messageExitMenu(label:String){
    println(label)
}

