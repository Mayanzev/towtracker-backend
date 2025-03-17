package com.mayantsev_vs.features.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseRemote(
    val login: String,
    val password: String,
    val username: String
)

@Serializable
data class UserReceiveRemote(
    val login: String,
    val password: String,
    val username: String
)