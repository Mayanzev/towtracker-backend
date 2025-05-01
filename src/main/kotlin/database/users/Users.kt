package com.mayantsev_vs.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Users: Table() {
    private val login = Users.varchar("login", 25)
    private val password = Users.varchar("password", 75)
    private val username = Users.varchar("username", 30)

    fun insert(userDBO: UserDBO) {
        transaction {
            Users.insert {
                it[login] = userDBO.login
                it[password] = userDBO.password
                it[username] = userDBO.username
            }
        }
    }

    fun fetchUser(login: String): UserDBO? {
        return try {
            transaction {
                val userModel = Users.selectAll().where { Users.login eq login }.single()
                UserDBO(
                    login = userModel[Users.login],
                    password = userModel[password],
                    username = userModel[username]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun updateUsername(usernameDBO: UsernameDBO) {
        transaction {
            Users.update ({ login eq usernameDBO.login }) {
                it[login] = usernameDBO.login
                it[username] = usernameDBO.username
            }
        }
    }

    fun updatePassword(passwordDBO: PasswordDBO) {
        transaction {
            Users.update ({ login eq passwordDBO.login }) {
                it[login] = passwordDBO.login
                it[password] = passwordDBO.password
            }
        }
    }

}