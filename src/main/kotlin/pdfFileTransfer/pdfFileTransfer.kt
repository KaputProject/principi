package pdfFileTransfer

import classes.PdfParser
import classes.Statement
import classes.StatementParameters
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
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.File
import org.json.JSONArray
import kotlinx.serialization.modules.contextual
import java.math.BigDecimal

@Serializable
data class UploadResponse(
    val message: String,
    val metadata: String?,
    val ime: String?,
    @Contextual val statement : Statement?
)

fun parseJsonArrayManual(jsonString: String): List<String> {
    val jsonArray = JSONArray(jsonString)
    return List(jsonArray.length()) { i -> jsonArray.getString(i) }
}

class PdfFileTransfer {

    // Shrani podatke zadnje naložene datoteke
    private var lastUpload: UploadResponse? = null

    fun startServer() {
        embeddedServer(Netty, port = 5001) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        encodeDefaults = true
                    }
                )
            }



            routing {
                post("/upload") {
                    val multipart = call.receiveMultipart()
                    var savedFilePath: String? = null
                    var metadataJson: String? = null
                    var imeJson: String? = null
                    var filename1: String? = null

                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FormItem -> {
                                when (part.name) {
                                    "metadata" -> {
                                        metadataJson = part.value
                                        println("Prejet JSON niz metadata: $metadataJson")
                                    }
                                    "ime" -> {
                                        imeJson = part.value
                                        println("Prejeto ime: $imeJson")
                                    }
                                }
                            }

                            is PartData.FileItem -> {
                                part.originalFileName?.let { filename ->
                                    val uploadDir = File("src/main/resources/uploads")
                                    if (!uploadDir.exists()) uploadDir.mkdirs()

                                    val file = File(uploadDir, File(filename).name)
                                    part.streamProvider().use { inputStream ->
                                        file.outputStream().buffered().use { outputStream ->
                                            inputStream.copyTo(outputStream)
                                        }
                                        filename1 = File(filename).name
                                        println("Prejeto filename1: $filename1")
                                    }
                                    savedFilePath = file.absolutePath
                                    println("Shranjena datoteka: $savedFilePath")
                                }
                            }

                            else -> Unit
                        }
                        part.dispose()
                    }
                    var path = savedFilePath
                    val parameters = if (path != null && imeJson != null && metadataJson != null) {
                        StatementParameters(
                            file = path,
                            user = imeJson!!,
                            partners = parseJsonArrayManual(metadataJson!!)
                        )
                    } else {
                        null
                    }

                    val parser = PdfParser()
                    val statement1 = parameters?.let { it1 -> parser.parse(it1) }
                    val response = if (savedFilePath != null && metadataJson != null) {

                        UploadResponse(
                            message = "Datoteka in metadata JSON uspešno prejeta!",
                            statement = statement1,
                            metadata = metadataJson,
                            ime = imeJson,
                        )

                    } else {
                        UploadResponse(
                            message = "Manjkajo datoteka ali metadata JSON.",
                            statement = statement1,
                            metadata = metadataJson,
                            ime = imeJson
                        )
                    }
                    val file = File(path)
                    if (file.exists()) {
                        if (file.delete()) {
                            println("File deleted successfully.")
                        } else {
                            println("Failed to delete the file.")
                        }
                    } else {
                        println("File does not exist.")
                    }
                    lastUpload = response
                    call.respond(if (savedFilePath != null && metadataJson != null) HttpStatusCode.OK else HttpStatusCode.BadRequest, response)
                }

            }
        }.start(wait = true)
    }


}
