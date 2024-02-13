package com.proteikotlin.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object ActiveSessions : Table() {
    val id = integer("id").autoIncrement()
    val token = varchar("token",50)

    override val primaryKey = PrimaryKey(id)
}
object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 20)
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
    val chatId = integer("chatId")
    val body = varchar("body",256)
    val timestamp = varchar("timestamp",100)


    override val primaryKey = PrimaryKey(id)
}

fun Application.createTables() {
    transaction(database) {
        SchemaUtils.create(Users)
        SchemaUtils.create(Chats)
        SchemaUtils.create(Messages)
        SchemaUtils.create(ActiveSessions)
    }
}