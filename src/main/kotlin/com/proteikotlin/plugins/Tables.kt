package com.proteikotlin.plugins

import org.jetbrains.exposed.sql.Table

object ActiveSessions : Table() {
    val id = integer("id").autoIncrement()
    val token = varchar("token",20)

    override val primaryKey = PrimaryKey(id)
}
object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val login = varchar("login",20)
    val password = varchar("password",20)

    override val primaryKey = PrimaryKey(id)
}
object Chats : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val usersNumber = integer("usersNumber")
    val usersList = text("usersList")

    override val primaryKey = PrimaryKey(id)
}
object Messages : Table() {
    val id = integer("id").autoIncrement()
    val sender = varchar("sender", 50)
    val chat = varchar("chat",20)
    val body = varchar("body",256)
    val timestamp = varchar("timestamp", 20)


    override val primaryKey = PrimaryKey(id)
}