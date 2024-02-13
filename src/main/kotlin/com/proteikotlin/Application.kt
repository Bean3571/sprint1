package com.proteikotlin

import com.proteikotlin.plugins.configureRouting
import com.proteikotlin.plugins.configureSerialization
import com.proteikotlin.plugins.createTables
import configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    configureSerialization()
    createTables()
    configureRouting()
    configureSockets()
}
