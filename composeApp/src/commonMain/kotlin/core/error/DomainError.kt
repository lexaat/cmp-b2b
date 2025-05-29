package core.error

/** Описываем ТОЛЬКО доменные ошибки, без кодов сервера. */
sealed class DomainError : Throwable() {
    object SessionExpired : DomainError()
    object OtpRequired : DomainError()
    data class Validation(val msg: String) : DomainError()
    object Network : DomainError()
    data class Unknown(val msg: String) : DomainError()
}