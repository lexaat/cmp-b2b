package networking

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent

val LoggingBodyPlugin = createClientPlugin("LoggingBodyPlugin") {
    onRequest { request, _ ->
        println("➡️ [REQUEST] ${request.method.value} ${request.url}")

        request.headers.entries().forEach { (name, values) ->
            println("  $name: ${values.joinToString()}")
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

        if (request.body !is OutgoingContent.NoContent) {
            println("  Body (raw object): ${request.body}")
        }
    }

    onResponse { response ->
        println("⬅️ [RESPONSE] ${response.status}")
        response.headers.entries().forEach { (name, values) ->
            println("  $name: ${values.joinToString()}")
        }

        val text = response.bodyAsText()
        println("  Body: $text")
    }
}
