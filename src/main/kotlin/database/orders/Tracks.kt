package com.mayantsev_vs.database.orders

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tracks: Table() {
    val id = Tracks.integer("id")
    val orderId = reference("orderId", Orders.id)
    val time = Tracks.varchar("time", 20)
    val date = Tracks.varchar("date", 20)
    val distance = Tracks.varchar("distance", 50)
    val speed = Tracks.varchar("speed", 20)
    val price = Tracks.varchar("price", 20)
    val firstCity = Tracks.varchar("firstCity", 50)
    val secondCity = Tracks.varchar("secondCity", 50)

    fun getTracks(orderId: Int): List<TracksDBO> {
        return transaction {
            Tracks.selectAll().where {
                Tracks.orderId eq orderId
            }.map {
                TracksDBO(
                    id = it[Tracks.id],
                    time = it[Tracks.time],
                    date = it[Tracks.date],
                    distance = it[Tracks.distance],
                    speed = it[Tracks.speed],
                    price = it[Tracks.price],
                    firstCity = it[Tracks.firstCity],
                    secondCity = it[Tracks.secondCity]
                )
            }
        }
    }

}