package com.mayantsev_vs.features.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderReceiveDTO(
    val tracks: List<TrackReceiveDTO>,
    val services: List<ServiceReceiveDTO>
)

@Serializable
data class OrderListResponseDTO(
    val orders: List<OrderResponseDTO>
)

@Serializable
data class OrderResponseDTO(
    val tracks: List<TrackResponseDTO>,
    val services: List<ServiceResponseDTO>,
    val date: String
)