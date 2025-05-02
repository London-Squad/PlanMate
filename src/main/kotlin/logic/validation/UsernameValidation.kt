package logic.validation

import logic.exceptions.AuthenticationException

fun String.takeIfValidNameOrThrowException(){
    if (!isValidUserName()) throw AuthenticationException.InvalidUserNameLengthException()
}

private fun String.isValidUserName() = length in 4..11