package com.mayantsev_vs

import com.mayantsev_vs.features.login.configureLoginRouting
import com.mayantsev_vs.features.register.configureRegisterRouting
import com.mayantsev_vs.features.user.configureUserRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/towtracker",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "125"
    )

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureUserRouting()
}
