package com.example.salesapp.data.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object JSONObjectToStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("JsonObjectAsString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        return decoder.decodeString()
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

}

@Serializable
data class Product (
    @Serializable(with = JSONObjectToStringSerializer::class) var data : String,
    val errMessage : String,
    val statusCode : String
)