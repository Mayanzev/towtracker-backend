package com.mayantsev_vs.features.order

import kotlinx.serialization.Serializable

@Serializable
data class ServiceRequestDTO(
    val id: Int,
    val name: String,
    val price: String,
    val date: String
)