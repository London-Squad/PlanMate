package logic.validation

import logic.exception.AuthenticationException


fun String.takeIfValidNameOrThrowException(){
    if (!isValidUserName()) throw AuthenticationException.InvalidUserNameLengthException()
}

private fun String.isValidUserName() = length in 4..11