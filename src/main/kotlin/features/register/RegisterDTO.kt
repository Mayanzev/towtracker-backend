package com.mayantsev_vs.features.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDTO (
    val login: String,
    val password: String,
    val username: String
)

@Serializable
data class RegisterResponseDTO (
    val token: String
)