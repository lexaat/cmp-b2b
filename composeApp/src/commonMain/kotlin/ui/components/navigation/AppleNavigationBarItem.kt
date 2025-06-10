package ui.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp

@Composable
fun AppleNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)? = null,
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(selectedIconColor = Red),
){
    val interactionSource = remember { MutableInteractionSource() }
    //val tabNavigator = LocalTabNavigator.current

    Box(modifier = modifier
        .height(48.dp)
        .wrapContentWidth()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onClick() }){
        Column(modifier = Modifier
            .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            icon()
            if (label != null) {
                label()
            }
        }
    }
}