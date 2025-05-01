package com.mayantsev_vs.database.orders

import java.time.LocalDateTime

class OrdersDBO (
    val id: Int = -1,
    val date: LocalDateTime,
    val tracks: List<TracksDBO>,
    val services: List<ServicesDBO>,
    val login: String
)