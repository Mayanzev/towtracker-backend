package com.mayantsev_vs.database.orders

import java.time.LocalDateTime

class TracksDTO(
    val id: Int,
    val time: String,
    val date: String,
    val distance: String,
    val speed: String,
    val price: String,
    val firstCity: String,
    val secondCity: String
)