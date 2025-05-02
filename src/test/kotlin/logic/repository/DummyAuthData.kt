package logic.repository

import logic.entities.User
import java.util.*

object DummyAuthData {
    val users = List(3) { User(UUID.fromString("b7d602f8-1cf0-4f8d-b409-35c8d3e0bcd$it"), "name$it", User.Type.MATE) }
    val usersAsString = List(3) { "${UUID.fromString("b7d602f8-1cf0-4f8d-b409-35c8d3e0bcd$it")},name$it,${User.Type.MATE}" }
}