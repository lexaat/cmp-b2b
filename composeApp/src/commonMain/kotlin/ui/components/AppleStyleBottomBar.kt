package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import features.main.presentation.MainScreen
import ui.components.navigation.AppleNavigationBarItem


@Composable
fun AppleStyleBottomBar(
    selectedIndex: Int,
    items: List<MainScreen.BottomNavItem>,
    onSelect: (Int) -> Unit,
    backgroundColor: Color,
    alpha: Float,
    modifier: Modifier = Modifier,
    topAppBarHeight: Dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val density = LocalDensity.current
    val bottomInset = WindowInsets.navigationBars.getBottom(density)
    val bottomInsetDp = with(density) { bottomInset.toDp() }

    // Получаем высоту безопасной области (нижний inset)
    val bottomPadding = with(LocalDensity.current) {
        WindowInsets.safeDrawing
            .getBottom(this)
            .toDp()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp + bottomPadding) // Учитываем безопасную область
            .background(backgroundColor.copy(alpha = alpha))
            .padding(bottom = bottomPadding) // Добавляем отступ для контента внутри панели
    ) {
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = alpha))
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                AppleNavigationBarItem(
                    selected = index == selectedIndex,
                    onClick = { onSelect(index) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(item.label) },
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
