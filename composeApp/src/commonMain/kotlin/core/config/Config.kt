package config

enum class Environment {
    DEV, STAGING, PROD
}

data class AppConfig(
    val baseUrl: String,
    val logLevel: LogLevel,
    val timeoutSeconds: Int,
    val isMockEnabled: Boolean
)

enum class LogLevel {
    NONE, BASIC, HEADERS, BODY
}

expect fun getEnvironment(): Environment

expect object Config {
    val current: AppConfig
}