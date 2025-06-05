package ui.components

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object FlexibleLongAsStringSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleLongAsString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String?) {
        encoder.encodeString(value ?: "")
    }

    override fun deserialize(decoder: Decoder): String? {
        return try {
            decoder.decodeString()
        } catch (e: Exception) {
            try {
                decoder.decodeLong().toString()
            } catch (e: Exception) {
                null
            }
        }
    }
}
