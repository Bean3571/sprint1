
import com.proteikotlin.plugins.ExposedMessage
import com.proteikotlin.plugins.messageService
import com.proteikotlin.plugins.sessionService
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}

fun Application.configureSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(60)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

        webSocket("/chat") {
            val token: String = call.request.headers["X-Auth-Token"].toString()
            if (sessionService.read(token) != null){
                println("Adding user!")
                val thisConnection = Connection(this)
                connections += thisConnection
                try {
                    send("You are connected! There are ${connections.count()} users online.")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val message = Json.decodeFromString<ExposedMessage>(frame.readText())
                        messageService.create(message)
                        val messageFrame = "${message.sender}: ${message.body} \n [${message.timestamp}]"
                        connections.forEach {
                            it.session.send(messageFrame)
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing $thisConnection!")
                    connections -= thisConnection
                }
            }
        }

    }
}