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
                call.respond(HttpStatusCode.BadRequest, "Пользователь не найден")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Срок действия токена истек")
        }
    }

    suspend fun updateUser() {
        val token = call.request.headers["Bearer-Authorization"]
        val userRequestDTO = call.receive<UserRequestDTO>()

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val usernameDBO = UsernameDBO(
                login = userRequestDTO.login,
                username = userRequestDTO.username
            )
            Users.updateUsername(usernameDBO)
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Срок действия токена истек")
        }
    }

    suspend fun updateUserPassword() {
        val token = call.request.headers["Bearer-Authorization"]
        val userPasswordRequestDTO = call.receive<UserPasswordRequestDTO>()
        val userRequestDTO = Users.fetchUser(userPasswordRequestDTO.login)

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            if (userRequestDTO == null) {
                call.respond(HttpStatusCode.BadRequest, "Пользователь не найден")
            } else if (verifyPassword(userPasswordRequestDTO.password, userRequestDTO.password)) {
                val passwordDBO = PasswordDBO(
                    login = userPasswordRequestDTO.login,
                    password = hashPassword(userPasswordRequestDTO.newPassword)
                )
                Users.updatePassword(passwordDBO)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Старый пароль введен неверно")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Срок действия токена истек")
        }
    }

}