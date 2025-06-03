package networking

import com.google.gson.GsonBuilder
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent

val gson = GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation() // Опционально: убирает поля без @Expose
    .serializeNulls() // чтобы видеть null-поля
    .create()

val LoggingBodyPlugin = createClientPlugin("LoggingBodyPlugin") {
    onRequest { request, _ ->
        println("➡️ [REQUEST] ${request.method.value} ${request.url}")
        request.headers.names().forEach { name ->
            val values = request.headers.getAll(name)?.joinToString() ?: ""
            println("  $name: $values")
        }

        request.headers[HttpHeaders.Authorization]?.let { authHeader ->
            if (authHeader.startsWith("Bearer ")) {
                val token = authHeader.removePrefix("Bearer ").trim()
                val preview = if (token.length > 10) {
                    "${token.take(5)}...${token.takeLast(5)}"
                } else token
                println("    ➡️ Authorization token preview: Bearer $preview")
            }
        }

        val body = request.body
        if (body !is OutgoingContent.NoContent) {
            try {
                val json = gson.toJson(body)
                println("  Body (JSON): $json")
            } catch (e: Exception) {
                println("  Body: $body (⚠️ сериализация не удалась: ${e.message})")
            }
        }
    }

    onResponse { response ->
        val status = response.status
        println("⬅️ [RESPONSE] $status")
        response.headers.names().forEach { name ->
            val values = response.headers.getAll(name)?.joinToString() ?: ""
            println("  $name: $values")
        }

        val responseBody = response.bodyAsText()
        println("  Body: $responseBody")
    }
}
