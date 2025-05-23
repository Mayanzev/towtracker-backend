package com.mayantsev_vs.features.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequestDTO(
    val tracks: List<TrackRequestDTO>,
    val services: List<ServiceRequestDTO>
)

@Serializable
data class OrderListResponseDTO(
    val orders: List<OrderResponseDTO>
)

@Serializable
data class OrderResponseDTO(
    val date: String,
    val price: String
)