package com.mayantsev_vs.features.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderReceiveRemote(
    val tracks: List<TrackReceiveRemote>,
    val services: List<ServiceReceiveRemote>
)

@Serializable
data class OrderListResponseRemote(
    val orders: List<OrderResponseRemote>
)

@Serializable
data class OrderResponseRemote(
    val tracks: List<TrackResponseRemote>,
    val services: List<ServiceResponseRemote>,
    val date: String
)