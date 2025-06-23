package features.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T> SharedFlow<T>.collectInLaunchedEffect(
    key: Any = Unit,
    onCollect: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = key) {
        this@collectInLaunchedEffect.collectLatest {
            onCollect(it)
        }
    }
}