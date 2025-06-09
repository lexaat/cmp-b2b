package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import features.main.presentation.MainScreen


@Composable
fun AppleStyleBottomBar(
    selectedIndex: Int,
    items: List<MainScreen.BottomNavItem>,
    onSelect: (Int) -> Unit
) {
    val density = LocalDensity.current
    val bottomInset = WindowInsets.navigationBars.getBottom(density)
    val bottomInsetDp = with(density) { bottomInset.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp + bottomInsetDp)
    ) {
        BlurredBackground(
            modifier = Modifier
                .matchParentSize()
        )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            tonalElevation = 0.dp,
            containerColor = Color.Transparent
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == selectedIndex,
                    onClick = { onSelect(index) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(item.label) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        indicatorColor = Color.Transparent // ⛔️ убираем овал
                    )
                )
            }
        }
    }
}
