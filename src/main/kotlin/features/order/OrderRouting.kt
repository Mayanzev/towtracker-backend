package com.mayantsev_vs.features.order

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureOrderRouting() {
    routing {
        post("/order/insert") {
            val orderController = OrderController(call)
            orderController.insertOrder()
        }
        get("/order/get") {
            val orderController = OrderController(call)
            orderController.getOrderList()
        }
    }
}