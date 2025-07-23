package com.kerymov.data_core.preferences

import androidx.datastore.core.Serializer
import com.kerymov.data_core.models.PreferencesDto
import com.kerymov.data_core.utils.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

class PreferencesSerializer(
    override val defaultValue: PreferencesDto,
    private val crypto: Crypto
) : Serializer<PreferencesDto> {

    override suspend fun readFrom(input: InputStream): PreferencesDto {
        val encryptedBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
        val decryptedBytes = crypto.decrypt(encryptedBytesDecoded)
        val json = decryptedBytes.decodeToString()

        return Json.decodeFromString(json)
    }

    override suspend fun writeTo(t: PreferencesDto, output: OutputStream) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = crypto.encrypt(bytes)
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)

        withContext(Dispatchers.IO) {
            output.use { it.write(encryptedBytesBase64) }
        }
    }
}