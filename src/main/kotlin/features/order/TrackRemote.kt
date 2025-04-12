package com.mayantsev_vs.features.order

import kotlinx.serialization.Serializable

@Serializable
data class TrackReceiveRemote(
    val id: Int,
    val time: String,
    val date: String,
    val distance: String,
    val speed: String,
    val price: String,
    val firstCity: String,
    val secondCity: String
)

@Serializable
data class TrackResponseRemote(
    val time: String,
    val date: String,
    val distance: String,
    val speed: String,
    val price: String,
    val firstCity: String,
    val secondCity: String
)