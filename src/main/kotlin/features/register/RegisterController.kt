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
        val registerRequestDTO = call.receive<RegisterRequestDTO>()
        val hashedPassword = hashPassword(registerRequestDTO.password)

        if (!registerRequestDTO.login.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDTO = Users.fetchUser(registerRequestDTO.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()

            try {
                Users.insert(
                    UserDBO(
                        login = registerRequestDTO.login,
                        password = hashedPassword,
                        username = registerRequestDTO.username
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }

            Tokens.insert(
                TokenDBO(
                    rowId = UUID.randomUUID().toString(), login = registerRequestDTO.login, token = token
                )
            )
            call.respond(RegisterResponseDTO(token = token))
        }
    }
}