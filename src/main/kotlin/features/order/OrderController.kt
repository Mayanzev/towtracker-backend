package com.mayantsev_vs.features.order

import com.mayantsev_vs.database.orders.*
import com.mayantsev_vs.database.tokens.Tokens
import com.mayantsev_vs.database.users.Users
import com.mayantsev_vs.utils.TokenCheck
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.time.LocalDateTime

class OrderController(private val call: ApplicationCall) {

    suspend fun insertOrder() {
        val token = call.request.headers["Bearer-Authorization"]
        val orderRequestDTO = call.receive<OrderRequestDTO>()

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val login = token?.let { Tokens.fetchLogin(it) }
            val user = login?.let { Users.fetchUser(login) }
            if (user != null) {
                Orders.insert(
                    OrdersDBO(
                        date = LocalDateTime.now(),
                        tracks = orderRequestDTO.tracks.map {
                            TracksDBO(
                                it.id,
                                it.time,
                                it.date,
                                it.distance,
                                it.speed,
                                it.price,
                                it.firstCity,
                                it.secondCity
                            )
                        },
                        services = orderRequestDTO.services.map {
                            ServicesDBO(
                                it.id,
                                it.name,
                                it.price,
                                it.date
                            )
                        },
                        login = login
                    )
                )
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun getOrderList() {
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val login = token?.let { Tokens.fetchLogin(it) }
            val user = login?.let { Users.fetchUser(login) }
            if (user != null) {
                val orderListRemote = OrderListResponseDTO(
                    Orders.getOrders(login).map { order ->
                        OrderResponseDTO(
                            order.tracks.map {
                                TrackResponseDTO(
                                    time = it.time,
                                    date = it.date,
                                    distance = it.distance,
                                    speed = it.speed,
                                    price = it.price,
                                    firstCity = it.firstCity,
                                    secondCity = it.secondCity
                                )
                            },
                            order.services.map {
                                ServiceResponseDTO(
                                    name = it.name,
                                    price = it.price,
                                    date = it.date
                                )
                            },
                            date = order.date.toString()
                        )
                    }
                )
                call.respond(orderListRemote)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}