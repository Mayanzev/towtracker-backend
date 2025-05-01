package com.mayantsev_vs.features.register

import com.mayantsev_vs.database.tokens.TokenDBO
import com.mayantsev_vs.database.tokens.Tokens
import com.mayantsev_vs.database.users.UserDBO
import com.mayantsev_vs.database.users.Users
import com.mayantsev_vs.utils.hashPassword
import com.mayantsev_vs.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveDTO = call.receive<RegisterReceiveDTO>()
        val hashedPassword = hashPassword(registerReceiveDTO.password)

        if (!registerReceiveDTO.login.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDTO = Users.fetchUser(registerReceiveDTO.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()

            try {
                Users.insert(
                    UserDBO(
                        login = registerReceiveDTO.login,
                        password = hashedPassword,
                        username = registerReceiveDTO.username
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }

            Tokens.insert(
                TokenDBO(
                    rowId = UUID.randomUUID().toString(), login = registerReceiveDTO.login, token = token
                )
            )
            call.respond(RegisterResponseDTO(token = token))
        }
    }
}