package com.mayantsev_vs.database.orders

import java.time.LocalDateTime

class OrdersDTO (
    val id: Int = -1,
    val date: LocalDateTime,
    val tracks: List<TracksDTO>,
    val services: List<ServicesDTO>,
    val login: String
)