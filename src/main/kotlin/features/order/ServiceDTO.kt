package com.mayantsev_vs.features.order

import kotlinx.serialization.Serializable

@Serializable
data class ServiceReceiveDTO(
    val id: Int,
    val name: String,
    val price: String,
    val date: String
)

@Serializable
data class ServiceResponseDTO(
    val name: String,
    val price: String,
    val date: String
)