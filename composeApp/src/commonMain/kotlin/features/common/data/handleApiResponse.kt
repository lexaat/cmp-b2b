package features.common.data

import data.model.ApiResponse

class ApiException(val code: Int, override val message: String) : Exception(message)

inline fun <T> ApiResponse<T>.requireResult(): T {
    if (error != null) {
        throw ApiException(error.code, error.message.toString())
    }
    return result ?: throw ApiException(-1, "Пустой результат")
}