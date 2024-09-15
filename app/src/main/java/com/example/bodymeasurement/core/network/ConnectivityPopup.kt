package com.example.bodymeasurement.core.network

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConnectivityPopup(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    text: String,
    backgroundColor: Color,
    iconRes: Painter
) {

    AnimatedVisibility(
        visible = !isConnected,
        enter = slideInVertically(tween(durationMillis = 500, easing = LinearEasing), initialOffsetY = { -40 }),
        exit = slideOutVertically(tween(durationMillis = 500, easing = LinearEasing), targetOffsetY = { -40 }),
        modifier = Modifier.fillMaxWidth()
    ) {


        Box(
            modifier = modifier
                .background(backgroundColor)
                .padding(16.dp)
                .fillMaxWidth()
                .height(30.dp),
            contentAlignment = Alignment.Center
        ){

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = iconRes,
                    tint = Color.White,
                    contentDescription = "network icon"
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

            }


        }

    }

}