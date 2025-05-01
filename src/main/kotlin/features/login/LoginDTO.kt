package com.mayantsev_vs.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveDTO(
    val login: String,
    val password: String
)

@Serializable
data class LoginResponseDTO(
    val token: String
)