package com.proteikotlin.plugins

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

// Модель Пользователя
@Serializable
data class ExposedUser(val name: String, val login: String, val password: String)
class UserService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 50)
        val login = varchar("login",20)
        val password = varchar("password",20)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: ExposedUser): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[login] = user.login
            it[password] = user.password
        }[Users.id]
    }

    suspend fun read(id: Int): ExposedUser? {
        return dbQuery {
            Users.select { Users.id eq id }
                .map { ExposedUser(it[Users.name], it[Users.login], it[Users.password]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[login] = user.login
                it[password] = user.password
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}

// Модель чата
@Serializable
data class ExposedChat(val name: String, val usersNumber: Int, val usersList: String)
class ChatService(private val database: Database) {
    object Chats : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 50)
        val usersNumber = integer("usersNumber")
        val usersList = text("usersList")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Chats)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(chat: ExposedChat): Int = dbQuery {
        Chats.insert {
            it[name] = chat.name
            it[usersNumber] = chat.usersNumber
            it[usersList] = chat.usersList
        }[Chats.id]
    }

    suspend fun read(id: Int): ExposedChat? {
        return dbQuery {
            Chats.select { Chats.id eq id }
                .map { ExposedChat(it[Chats.name], it[Chats.usersNumber], it[Chats.usersList]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, chat: ExposedChat) {
        dbQuery {
            Chats.update({ Chats.id eq id }) {
                it[name] = chat.name
                it[usersNumber] = chat.usersNumber
                it[usersList] = chat.usersList
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Chats.deleteWhere { Chats.id.eq(id) }
        }
    }
}

// Модель сообщения
@Serializable
data class ExposedMessage(val sender: String, val chat: String, val body: String)
class MessageService(private val database: Database) {
    object Messages : Table() {
        val id = integer("id").autoIncrement()
        val sender = varchar("sender", 50)
        val chat = varchar("chat",20)
        val body = varchar("body",256)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Messages)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(message: ExposedMessage): Int = dbQuery {
        Messages.insert {
            it[sender] = message.sender
            it[chat] = message.chat
            it[body] = message.body
        }[Messages.id]
    }

    suspend fun read(id: Int): ExposedMessage? {
        return dbQuery {
            Messages.select { Messages.id eq id }
                .map {ExposedMessage(
                    it[Messages.sender],
                    it[Messages.chat],
                    it[Messages.body]
                )}
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, message: ExposedMessage) {
        dbQuery {
            Messages.update({ Messages.id eq id }) {
                it[sender] = message.sender
                it[chat] = message.chat
                it[body] = message.body
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Messages.deleteWhere { Messages.id.eq(id) }
        }
    }
}