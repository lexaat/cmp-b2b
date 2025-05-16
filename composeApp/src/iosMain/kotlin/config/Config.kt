package config

import platform.Foundation.NSBundle

actual fun getEnvironment(): Environment {
    val value = NSBundle.mainBundle.infoDictionary?.get("ENVIRONMENT") as? String
    return when (value) {
        "dev" -> Environment.DEV
        "staging" -> Environment.STAGING
        "prod" -> Environment.PROD
        else -> error("Unknown environment: $value")
    }
}

actual object Config {
    actual val current: AppConfig
        get() = when (getEnvironment()) {
            Environment.DEV -> AppConfig(
                baseUrl = "https://b2b-test.hayotbank.uz/mobile.svc",
                logLevel = LogLevel.BODY,
                timeoutSeconds = 60,
                isMockEnabled = true
            )

            Environment.STAGING -> AppConfig(
                baseUrl = "https://b2b-test.hayotbank.uz/mobile.svc",
                logLevel = LogLevel.HEADERS,
                timeoutSeconds = 45,
                isMockEnabled = false
            )

            Environment.PROD -> AppConfig(
                baseUrl = "https://b2b.hayotbank.uz/mobile.svc",
                logLevel = LogLevel.NONE,
                timeoutSeconds = 30,
                isMockEnabled = false
            )
        }
}