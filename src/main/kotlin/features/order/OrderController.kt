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
import java.time.format.DateTimeFormatter

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
                        date = LocalDateTime.now().withNano(0),
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
                call.respond(HttpStatusCode.BadRequest, "Пользователь не найден")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Срок действия токена истек")
        }
    }

    suspend fun getOrderList() {
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val login = token?.let { Tokens.fetchLogin(it) }
            val user = login?.let { Users.fetchUser(login) }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            if (user != null) {
                val orderListRemote = OrderListResponseDTO(
                    Orders.getOrders(login).map { order ->

                        var price = 0.0
                        order.tracks.forEach { track ->
                            price += track.price.toDouble()
                        }
                        order.services.forEach { service ->
                            price += service.price.toDouble()
                        }

                        OrderResponseDTO(
                            date = order.date.format(formatter),
                            price = price.toString()
                        )
                    }
                )
                call.respond(orderListRemote)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Пользователь не найден")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Срок действия токена истек")
        }
    }
}