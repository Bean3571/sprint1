package com.proteikotlin

import com.proteikotlin.plugins.ExposedUser
import com.proteikotlin.plugins.configureRouting
import com.proteikotlin.plugins.configureSerialization
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testReg() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        val client = createClient {

        }
        val response = client.post("/api/reg") {
            contentType(ContentType.Application.Json)
            setBody(ExposedUser("Kent Paul", "username", "password"))
        }
        assertEquals(HttpStatusCode.Created,response.status)
        assertEquals("1",response.bodyAsText())
    }


}
