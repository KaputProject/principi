import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import ui.dataClasses.locations.Location

@OptIn(InternalSerializationApi::class)
object LocationAsStringOrObjectSerializer : KSerializer<Location?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LocationOrString")

    override fun deserialize(decoder: Decoder): Location? {
        val input = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")
        return when (val element = input.decodeJsonElement()) {
            is JsonNull -> null
            is JsonObject -> input.json.decodeFromJsonElement(Location.serializer(), element)
            is JsonPrimitive -> {
                if (element.isString) {
                    Location(_id = "", name = element.content)
                } else {
                    null
                }
            }

            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: Location?) {
        val output = encoder as? JsonEncoder ?: error("Can be serialized only by JSON")
        if (value == null) {
            output.encodeNull()
        } else {
            output.encodeSerializableValue(Location.serializer(), value)
        }
    }
}
