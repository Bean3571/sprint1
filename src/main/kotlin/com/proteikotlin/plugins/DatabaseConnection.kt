package com.proteikotlin.plugins

import org.jetbrains.exposed.sql.Database

val database = Database.connect(
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "root",
    driver = "org.h2.Driver",
    password = ""
)

val chatService = ChatService(database)
val userService = UserService(database)
val messageService = MessageService(database)
val sessionService = SessionService(database)