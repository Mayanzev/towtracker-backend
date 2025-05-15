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
        val request = call.receive<RegisterRequestDTO>()

        if (!request.login.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Неверная форма email")
            return
        }

        if (Users.fetchUser(request.login) != null) {
            call.respond(HttpStatusCode.Conflict, "Такой пользователь уже существует")
            return
        }

        val hashedPassword = hashPassword(request.password)
        val token = UUID.randomUUID().toString()

        Users.insert(
            UserDBO(
                login = request.login,
                password = hashedPassword,
                username = request.username
            )
        )

        Tokens.insert(
            TokenDBO(
                rowId = UUID.randomUUID().toString(),
                login = request.login,
                token = token
            )
        )

        call.respond(RegisterResponseDTO(token = token))
    }
}