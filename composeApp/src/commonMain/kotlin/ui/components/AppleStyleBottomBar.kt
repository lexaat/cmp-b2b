package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import features.main.presentation.MainScreen
import ui.components.navigation.AppleNavigationBarItem
import ui.theme.LocalHazeState

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun AppleStyleBottomBar(
    selectedIndex: Int,
    items: List<MainScreen.BottomNavItem>,
    onSelect: (Int) -> Unit,
    alpha: Float,
    modifier: Modifier = Modifier
) {

    //println("AppleStyleBottomBar — alpha: $alpha")

    // Получаем высоту безопасной области (нижний inset)
    val bottomPadding = with(LocalDensity.current) {
        WindowInsets.safeDrawing
            .getBottom(this)
            .toDp()
    }

    val effectiveModifier = if (alpha > 0.3f) {
        Modifier.hazeEffect(
            state = LocalHazeState.current,
            style = HazeMaterials.ultraThin()
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp + bottomPadding)
            .then(effectiveModifier)
    ) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = alpha)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding)
                .padding(horizontal = 16.dp),

            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                if (item.label.equals("+")) {
                    // Центральная кнопка "+"
                    AppleNavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = { onSelect(index) },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Создать",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                } else {
                    AppleNavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = { onSelect(index) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null
                            )
                        },
                        label = { Text(item.label, fontSize = 11.sp) }
                    )
                }
            }
        }
    }
}
