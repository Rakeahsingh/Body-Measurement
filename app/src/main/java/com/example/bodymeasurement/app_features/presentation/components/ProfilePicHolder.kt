package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bodymeasurement.R
import com.example.bodymeasurement.ui.theme.CustomBlue
import com.example.bodymeasurement.ui.theme.CustomPink
import com.example.bodymeasurement.ui.theme.CustomRed
import com.example.bodymeasurement.ui.theme.CustomSky

@Composable
fun ProfilePicHolder(
    modifier: Modifier = Modifier,
    padding: Dp,
    borderWidth: Dp,
    placeHolderSize: Dp,
    profilePicUrl: String?
) {

    val imageRequest = ImageRequest
        .Builder(LocalContext.current)
        .data(profilePicUrl)
        .crossfade(true)
        .build()

    Box(
        modifier = modifier
            .size(placeHolderSize)
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(listOf(CustomBlue, CustomPink, CustomRed, CustomSky)),
                shape = CircleShape
            )
            .padding(padding)
    ){

        AsyncImage(
            modifier = modifier
                .fillMaxSize()
                .clip(CircleShape),
            model = imageRequest,
            contentScale = ContentScale.Crop,
            contentDescription = "profile pic",
            placeholder = painterResource(id = R.drawable.img),
            error = painterResource(id = R.drawable.img)
        )

    }

}