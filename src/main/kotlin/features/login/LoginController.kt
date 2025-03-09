package com.mayantsev_vs.features.register

import com.mayantsev_vs.cache.InMemoryCache
import com.mayantsev_vs.cache.TokenCache
import com.mayantsev_vs.database.tokens.TokenDTO
import com.mayantsev_vs.database.tokens.Tokens
import com.mayantsev_vs.database.users.UserDTO
import com.mayantsev_vs.database.users.Users
import com.mayantsev_vs.features.login.LoginReceiveRemote
import com.mayantsev_vs.features.login.LoginResponseRemote
import com.mayantsev_vs.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*
import kotlin.math.log

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        login = receive.login,
                        token = token
                    )
                )
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }

}