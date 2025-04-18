package com.mayantsev_vs.database.users

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Users: Table() {
    private val login = Users.varchar("login", 25)
    private val password = Users.varchar("password", 75)
    private val username = Users.varchar("username", 30)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.username
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.selectAll().where { Users.login eq login }.single()
                UserDTO(
                    login = userModel[Users.login],
                    password = userModel[password],
                    username = userModel[username]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun updateUsername(usernameDTO: UsernameDTO) {
        transaction {
            Users.update ({ login eq usernameDTO.login }) {
                it[login] = usernameDTO.login
                it[username] = usernameDTO.username
            }
        }
    }

    fun updatePassword(passwordDTO: PasswordDTO) {
        transaction {
            Users.update ({ login eq passwordDTO.login }) {
                it[login] = passwordDTO.login
                it[password] = passwordDTO.password
            }
        }
    }

}