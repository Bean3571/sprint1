package com.proteikotlin.plugins

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

class SessionService(private val database: Database) {
    suspend fun create(newToken: String): Int = dbQuery {
        ActiveSessions.insert {
            it[token] = newToken
        }[ActiveSessions.id]
    }

    suspend fun read(token: String): Session? {
        return dbQuery {
            ActiveSessions.select { ActiveSessions.token eq token }
                .map { Session(
                    it[ActiveSessions.token]
                ) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, session: Session) {
        dbQuery {
            ActiveSessions.update({ ActiveSessions.id eq id }) {
                it[token] = session.token
            }
        }
    }

    suspend fun delete(token: String) {
        dbQuery {
            ActiveSessions.deleteWhere { ActiveSessions.token.eq(token) }
        }
    }
}

class UserService(private val database: Database) {

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
                .map { ExposedUser(
                    it[Users.name],
                    it[Users.login],
                    it[Users.password]
                ) }
                .singleOrNull()
        }
    }
    suspend fun read(login: String): ExposedUser? {
        return dbQuery {
            Users.select { Users.login eq login }
                .map { ExposedUser(
                    it[Users.name],
                    it[Users.login],
                    it[Users.password]
                ) }
                .singleOrNull()
        }
    }
    suspend fun read(login: String, password: String): ExposedUser? {
        return dbQuery {
            Users.select { Users.login eq login; Users.password eq password }
                .map { ExposedUser(
                    it[Users.name],
                    it[Users.login],
                    it[Users.password]
                ) }
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

class ChatService(private val database: Database) {

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
                .map { ExposedChat(
                    it[Chats.name],
                    it[Chats.usersNumber],
                    it[Chats.usersList]
                ) }
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

class MessageService(private val database: Database) {

    suspend fun create(message: ExposedMessage): Int = dbQuery {
        Messages.insert {
            it[sender] = message.sender
            it[chatId] = message.chatId
            it[body] = message.body
            it[timestamp] = message.timestamp
        }[Messages.id]
    }

    suspend fun read(id: Int): ExposedMessage? {
        return dbQuery {
            Messages.select { Messages.id eq id }
                .map {ExposedMessage(
                    it[Messages.sender],
                    it[Messages.chatId],
                    it[Messages.body],
                    it[Messages.timestamp]
                )}
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, message: ExposedMessage) {
        dbQuery {
            Messages.update({ Messages.id eq id }) {
                it[sender] = message.sender
                it[chatId] = message.chatId
                it[body] = message.body
                it[timestamp] = message.timestamp
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Messages.deleteWhere { Messages.id.eq(id) }
        }
    }
}