package pdfFileTransfer

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class PdfFileTransfer {
    fun startServer() {
        embeddedServer(Netty, port = 80) {
            routing {
                post("/") {
                    val jsonText = call.receiveText()
                    println("Prejeti JSON:")
                    println(jsonText)
                    call.respondText("JSON prejet", status = HttpStatusCode.OK)
                }
            }
        }.start(wait = true)
    }
}
