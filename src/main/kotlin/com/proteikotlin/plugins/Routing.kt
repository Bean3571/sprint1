package com.proteikotlin.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/protei") {
            call.respondText("Hello Protei!")
        }
    }
}
