package core.model

data class ResultWithEffect<T, SIDE_EFFECT>(
    val result: T? = null,
    val sideEffect: SIDE_EFFECT? = null
)
