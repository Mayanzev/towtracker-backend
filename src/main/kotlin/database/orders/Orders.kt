package com.mayantsev_vs.database.orders

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

object Orders : Table() {
    val id = Orders.integer("id")
    private val date = datetime("date")
    private val login = Orders.varchar("login", 25)

    fun insert(orderDTO: OrdersDBO) {
        transaction {
            val rowId = Orders.insertReturning(listOf(Orders.id)) {
                it[date] = orderDTO.date
                it[login] = orderDTO.login
            }.map { it[Orders.id] }

            orderDTO.tracks.forEach { trackDTO ->
                Tracks.insert {
                    it[id] = trackDTO.id
                    it[orderId] = rowId.first()
                    it[time] = trackDTO.time
                    it[date] = trackDTO.date
                    it[distance] = trackDTO.distance
                    it[speed] = trackDTO.speed
                    it[price] = trackDTO.price
                    it[firstCity] = trackDTO.firstCity
                    it[secondCity] = trackDTO.secondCity
                }
            }

            orderDTO.services.forEach { serviceDTO ->
                Services.insert {
                    it[id] = serviceDTO.id
                    it[orderId] = rowId.first()
                    it[name] = serviceDTO.name
                    it[price] = serviceDTO.price
                    it[date] = serviceDTO.date
                }
            }
        }
    }

    fun getOrders(login: String): List<OrdersDBO> {
        return transaction {
            Orders.selectAll().where {
                Orders.login eq login
            }.orderBy(Orders.date, SortOrder.DESC).map {

                val tracks = Tracks.getTracks(it[Orders.id])
                val services = Services.getServices(it[Orders.id])

                OrdersDBO(
                    id = it[Orders.id],
                    date = it[Orders.date],
                    tracks = tracks,
                    services = services,
                    login = it[Orders.login]
                )
            }
        }
    }

}
