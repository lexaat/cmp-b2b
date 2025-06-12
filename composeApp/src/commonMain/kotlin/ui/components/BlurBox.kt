package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.CupertinoMaterials
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun BlurBox(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    style: HazeStyle = CupertinoMaterials.thin(),
    content: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        Box(
            Modifier
                .matchParentSize()
                .hazeEffect(
                    state = hazeState,
                    style = style
                )
        )
        content()
    }
}