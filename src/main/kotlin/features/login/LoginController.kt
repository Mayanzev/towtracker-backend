package com.mayantsev_vs.features.login

import com.mayantsev_vs.database.tokens.TokenDBO
import com.mayantsev_vs.database.tokens.Tokens
import com.mayantsev_vs.database.users.Users
import com.mayantsev_vs.utils.verifyPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveDTO>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (verifyPassword(receive.password, userDTO.password)) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDBO(
                        rowId = UUID.randomUUID().toString(),
                        login = receive.login,
                        token = token
                    )
                )
                call.respond(LoginResponseDTO(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }

}