package com.mayantsev_vs.features.user

import com.mayantsev_vs.database.tokens.Tokens
import com.mayantsev_vs.database.users.PasswordDTO
import com.mayantsev_vs.database.users.UsernameDTO
import com.mayantsev_vs.database.users.Users
import com.mayantsev_vs.features.login.LoginReceiveRemote
import com.mayantsev_vs.utils.TokenCheck
import com.mayantsev_vs.utils.hashPassword
import com.mayantsev_vs.utils.verifyPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserController(private val call: ApplicationCall) {

    suspend fun fetchUser() {
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val login = token?.let { Tokens.fetchLogin(it) }
            val user = login?.let { Users.fetchUser(login) }
            if (user != null) {
                val userRemote = UserResponseRemote(
                    login = user.login,
                    username = user.username
                )
                call.respond(userRemote)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun updateUser() {
        val token = call.request.headers["Bearer-Authorization"]
        val userReceiveRemote = call.receive<UserReceiveRemote>()

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val usernameDTO = UsernameDTO(
                login = userReceiveRemote.login,
                username = userReceiveRemote.username
            )
            Users.updateUsername(usernameDTO)
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun updateUserPassword() {
        val token = call.request.headers["Bearer-Authorization"]
        val userPasswordReceiveRemote = call.receive<UserPasswordReceiveRemote>()
        val userDTO = Users.fetchUser(userPasswordReceiveRemote.login)

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            if (userDTO == null) {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else if (verifyPassword(userPasswordReceiveRemote.password, userDTO.password)) {
                val passwordDTO = PasswordDTO(
                    login = userPasswordReceiveRemote.login,
                    password = hashPassword(userPasswordReceiveRemote.newPassword)
                )
                Users.updatePassword(passwordDTO)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Старый пароль введен неверно!")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

}