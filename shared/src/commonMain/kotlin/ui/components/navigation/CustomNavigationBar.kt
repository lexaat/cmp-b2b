package ui.components.navigation

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import features.home.presentation.HomeTab


@Composable
fun  CustomNavigationBar(
    backgroundColor: Color,
    alpha: Float,
    modifier: Modifier = Modifier,
    topAppBarHeight: Dp,
    onScrollStateReturned: (Dp, Dp) -> Unit
) {
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
            CustomNavigationBarItem(
                tab = HomeTab(
                    onScrollStateReturned = onScrollStateReturned,
                    topAppBarHeight = topAppBarHeight
                ),
            )
            CustomNavigationBarItem(
                tab = HomeTab(
                    onScrollStateReturned = onScrollStateReturned,
                    topAppBarHeight = topAppBarHeight
                ),
            )
            CustomNavigationBarItem(
                tab = HomeTab(
                    onScrollStateReturned = onScrollStateReturned,
                    topAppBarHeight = topAppBarHeight
                ),
            )
        }
    }
}

