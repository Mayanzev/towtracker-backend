package com.mayantsev_vs.database.orders

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Services: Table() {
    val id = Services.integer("id")
    val orderId = reference("orderId", Orders.id)
    val name = Services.varchar("name", 100)
    val price = Services.varchar("price", 20)
    val date = Services.varchar("date", 20)

    fun getServices(orderId: Int): List<ServicesDBO> {
        return transaction {
            Services.selectAll().where {
                Services.orderId eq orderId
            }.map {
                ServicesDBO(
                    id = it[Services.id],
                    name = it[Services.name],
                    price = it[Services.price],
                    date = it[Services.date]
                )
            }
        }
    }
}