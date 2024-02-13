package com.proteikotlin.plugins

import kotlinx.serialization.Serializable

//Модель данных для входа
@Serializable
data class Credentials(val login: String, val password: String)
data class Session(val token: String)

// Модель Пользователя
@Serializable
data class ExposedUser(val name: String, val login: String, val password: String)

// Модель чата
@Serializable
data class ExposedChat(val name: String, val usersNumber: Int, val usersList: String)

// Модель сообщения
@Serializable
data class ExposedMessage(val sender: String, val chatId: Int, val body: String, val timestamp: String)
