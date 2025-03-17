package com.mayantsev_vs.features.user

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserRouting() {
    routing {
        get("/user/fetch") {
            val userController = UserController(call)
            userController.fetchUser()
        }
        post("user/update") {
            val userController = UserController(call)
            userController.updateUser()
        }
    }
}

