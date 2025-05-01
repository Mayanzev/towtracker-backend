package com.mayantsev_vs.features.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    val login: String,
    val username: String
)

@Serializable
data class UserRequestDTO(
    val login: String,
    val username: String
)

@Serializable
data class UserPasswordRequestDTO(
    val login: String,
    val password: String,
    val newPassword: String
)