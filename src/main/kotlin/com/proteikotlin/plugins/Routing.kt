package com.proteikotlin.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRouting() {
    routing {
        // Create user
        post("/api/reg") {
            val user = call.receive<ExposedUser>()
            if (userService.read(user.login) != null){
                call.respond(HttpStatusCode.Found)
            } else {
                val id = userService.create(user)
                call.respond(HttpStatusCode.Created, id)
            }
        }

        post("/api/auth/login"){
            val credentials = call.receive<Credentials>()
            if (userService.read(credentials.login, credentials.password) == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {
                val notEncodedToken = "${credentials.login}:${credentials.password}"
                val token: String = Base64.getEncoder().encodeToString(notEncodedToken.toByteArray())
                sessionService.create(token)
                call.respond(HttpStatusCode.OK, token)
            }
        }

        post("/api/auth/logout"){
            val token: String = call.request.headers["X-Auth-Token"].toString()
            sessionService.delete(token)
            call.respond(HttpStatusCode.OK,"logged out successfully, Token deleted")
        }

        post("/api/auth/check-token"){
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized,"No active session with this token")
            } else {
                call.respond(HttpStatusCode.OK,"Token is valid")
            }
        }
        // Chats
        // Create chat
        post("/chats") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val chat = call.receive<ExposedChat>()
                val id = chatService.create(chat)
                call.respond(HttpStatusCode.Created, id)
            }
        }
        // Read chat
        get("/chats/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val chat = chatService.read(id)
                if (chat != null) {
                    call.respond(HttpStatusCode.OK, chat)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
        // Update chat
        put("/chats/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val chat = call.receive<ExposedChat>()
                chatService.update(id, chat)
                call.respond(HttpStatusCode.OK)
            }
        }
        // Delete chat
        delete("/chats/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                chatService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
        // Users
        // Read user
        get("/users/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
        // Update user
        put("/users/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = call.receive<ExposedUser>()
                userService.update(id, user)
                call.respond(HttpStatusCode.OK)
            }
        }
        // Delete user
        delete("/users/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                userService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
        // Messages
        // Create message
        post("/messages") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val message = call.receive<ExposedMessage>()
                val id = messageService.create(message)
                call.respond(HttpStatusCode.Created, id)
            }
        }
        // Read message
        get("/messages/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val message = messageService.read(id)
                if (message != null) {
                    call.respond(HttpStatusCode.OK, message)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
        // Update message
        put("/messages/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val message = call.receive<ExposedMessage>()
                messageService.update(id, message)
                call.respond(HttpStatusCode.OK)
            }
        }
        // Delete message
        delete("/messages/{id}") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) == null){
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                messageService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
