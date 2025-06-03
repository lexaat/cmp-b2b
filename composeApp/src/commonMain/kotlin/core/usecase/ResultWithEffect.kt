package core.usecase

data class ResultWithEffect<T, S>(
    val result: T? = null,
    val sideEffect: S? = null
)

