package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.domain.model.BodyPart
import com.example.bodymeasurement.app_features.presentation.details.DetailsEvent
import com.example.bodymeasurement.ui.theme.CustomYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    modifier: Modifier = Modifier,
    bodyPart: BodyPart?,
    onBackIconClick: () -> Unit,
    onAddIconClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onEvent: (DetailsEvent) -> Unit
){

    var isExpended by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = bodyPart?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        windowInsets = WindowInsets(0,0,0,0),
        navigationIcon = {
            IconButton(
                onClick = { onBackIconClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back icon"
                )
            }
        },
        actions = {
            FilledIconButton(
                onClick = { onAddIconClick() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = CustomYellow
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add icon"
                )
            }

            IconButton(
                onClick = { onDeleteIconClick() }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "delete icon"
                )
            }

            Spacer(modifier = Modifier.width(4.dp))
            
            Text(text = bodyPart?.measuringUnit ?: "")

            IconButton(
                onClick = { isExpended = true }
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "down icon"
                )
            }

            MeasuringUnitMenu(
                isExpended = isExpended,
                onDismissClick = { isExpended = false },
                onItemClick = { measuringUnit ->
                    onEvent(DetailsEvent.ChangeMeasuringUnit(measuringUnit))
                    isExpended = false
                }
            )

        }
    )


}