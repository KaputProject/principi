package pdfFileTransfer

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
//
//@Serializable
//data class User(val ime: String, val priimek: String)
//
//@Serializable
//data class UploadData(
//    val user: User,
//    val partners: List<String>
//)

class PdfFileTransfer {
    fun startServer() {

        embeddedServer(Netty, port = 5001) {
            install(ContentNegotiation) {
                json()
            }
            routing {
                post("/upload") {
                    val multipart = call.receiveMultipart()
                    var savedFilePath: String? = null
                    var metadataJson: String? = null

                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                if (part.originalFileName != null) {
                                    val uploadDir = File("src/main/resources/uploads")
                                    if (!uploadDir.exists()) {
                                        uploadDir.mkdirs()
                                    }

                                    val file = File(uploadDir, File(part.originalFileName!!).name)
                                    part.streamProvider().use { inputStream ->
                                        file.outputStream().buffered().use { outputStream ->
                                            inputStream.copyTo(outputStream)
                                        }
                                    }
                                    savedFilePath = file.absolutePath
                                    println("Shranjena datoteka: $savedFilePath")
                                }
                            }

                            is PartData.FormItem -> {
                                if (part.name == "metadata") {
                                    metadataJson = part.value
                                    println("Prejet JSON niz metadata: $metadataJson")
                                }
                            }

                            else -> {}
                        }
                        part.dispose()
                    }

                    if (savedFilePath != null && metadataJson != null) {
                        call.respondText("Datoteka in metadata JSON uspešno prejeta!", status = HttpStatusCode.OK)
                    } else {
                        call.respondText("Manjkajo datoteka ali metadata JSON.", status = HttpStatusCode.BadRequest)
                    }
                }

            }
        }.start(wait = true)
    }
}