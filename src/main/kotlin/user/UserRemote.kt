package com.mayantsev_vs.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseRemote(
    val login: String,
    val password: String,
    val username: String
)