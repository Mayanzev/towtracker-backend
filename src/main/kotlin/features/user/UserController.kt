package com.mayantsev_vs.features.user

import com.mayantsev_vs.database.tokens.Tokens
import com.mayantsev_vs.database.users.PasswordDBO
import com.mayantsev_vs.database.users.UsernameDBO
import com.mayantsev_vs.database.users.Users
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
                val userRemote = UserResponseDTO(
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
        val userReceiveDTO = call.receive<UserReceiveDTO>()

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val usernameDBO = UsernameDBO(
                login = userReceiveDTO.login,
                username = userReceiveDTO.username
            )
            Users.updateUsername(usernameDBO)
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun updateUserPassword() {
        val token = call.request.headers["Bearer-Authorization"]
        val userPasswordReceiveDTO = call.receive<UserPasswordReceiveDTO>()
        val userDTO = Users.fetchUser(userPasswordReceiveDTO.login)

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            if (userDTO == null) {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else if (verifyPassword(userPasswordReceiveDTO.password, userDTO.password)) {
                val passwordDBO = PasswordDBO(
                    login = userPasswordReceiveDTO.login,
                    password = hashPassword(userPasswordReceiveDTO.newPassword)
                )
                Users.updatePassword(passwordDBO)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Старый пароль введен неверно!")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

}