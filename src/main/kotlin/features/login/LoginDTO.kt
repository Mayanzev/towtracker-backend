package com.mayantsev_vs.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    val login: String,
    val password: String
)

@Serializable
data class LoginResponseDTO(
    val token: String
)