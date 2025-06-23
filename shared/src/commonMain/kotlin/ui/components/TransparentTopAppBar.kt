package ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import uz.hb.shared.SharedRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTopAppBar() {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(painterResource(SharedRes.images.logo_hayot_bank),"Hayot Bank")
            }
        },
        navigationIcon = {
            RoundedSquareLittleButton(
                icon = {
                    Icon(
                        painter = painterResource(SharedRes.images.ic_menu_icon_horizontal),
                        contentDescription = "Icon",
                        tint = Color(0xFF4A4A4A),
                        modifier = Modifier.size(24.dp)
                    )},
                onClick = TODO()
            )
        },
        actions = {
            RoundedSquareLittleButton(icon = {
                Icon(
                    painter = painterResource(SharedRes.images.ic_search),
                    contentDescription = "Icon",
                    tint = Color(0xFF4A4A4A),
                    modifier = Modifier.size(24.dp)
                )}) {}
            Spacer(modifier = Modifier.width(8.dp))
            RoundedSquareLittleButton(icon = {
                Icon(
                    painter = painterResource(SharedRes.images.ic_notification),
                    contentDescription = "Icon",
                    tint = Color(0xFF4A4A4A),
                    modifier = Modifier.size(24.dp)
                )}) {}
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )

}