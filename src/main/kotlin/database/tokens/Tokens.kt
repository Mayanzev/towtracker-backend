package com.mayantsev_vs.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens: Table() {
    private val id = Tokens.varchar("id", 50)
    private val login = Tokens.varchar("login", 25)
    private val token = Tokens.varchar("token", 50)

    fun insert(tokenDBO: TokenDBO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDBO.rowId
                it[login] = tokenDBO.login
                it[token] = tokenDBO.token
            }
        }
    }

    fun fetchTokens(): List<TokenDBO> {
        return try {
            transaction {
                Tokens.selectAll().toList()
                    .map {
                        TokenDBO(
                            rowId = it[Tokens.id],
                            token = it[token],
                            login = it[login]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchLogin(token: String): String? {
        return try {
            transaction {
                val login = Tokens.selectAll().where { Tokens.token eq token }.single()
                login[Tokens.login]
            }
        } catch (e: Exception) {
            null
        }
    }

}