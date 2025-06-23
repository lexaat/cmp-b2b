package ui.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab

@Composable
fun CustomNavigationBarItem(
    tab: Tab,
    modifier: Modifier = Modifier,
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(selectedIconColor = Red),
){
    val interactionSource = remember { MutableInteractionSource() }
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current == tab

    Box(modifier = modifier
        .height(48.dp)
        .wrapContentWidth()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) { tabNavigator.current = tab }){
        Column(modifier = Modifier
            .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title,
                modifier = modifier.size(24.dp),
                tint = if (selected) colors.selectedIconColor else colors.unselectedIconColor
            )
            Text(text = tab.options.title,
                fontSize = 12.sp,
                color = if (selected) colors.selectedIconColor else colors.unselectedIconColor)
        }
    }
}