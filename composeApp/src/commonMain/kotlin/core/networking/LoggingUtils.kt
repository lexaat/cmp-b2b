package core.networking

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> HttpRequestBuilder.setLoggedBody(
    body: T,
    contentType: ContentType = ContentType.Application.Json
) {
    val json = Json.encodeToString(body)
    println("📤 Body (JSON):\n$json")
    contentType(contentType)
    setBody(body)
}