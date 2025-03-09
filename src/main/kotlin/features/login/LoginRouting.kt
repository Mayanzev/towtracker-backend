package com.mayantsev_vs.features.login

import com.mayantsev_vs.cache.InMemoryCache
import com.mayantsev_vs.cache.TokenCache
import com.mayantsev_vs.features.register.LoginController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
    }
}