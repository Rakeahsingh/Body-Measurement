package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.R
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.core.utils.changeLocalDateToDateString
import com.example.bodymeasurement.core.utils.roundToDecimal

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistorySection(
    modifier: Modifier = Modifier,
    measuringUnitCode: String?,
    bodyPartsValue: List<BodyPartsValue>,
    onDeleteClick: (BodyPartsValue) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {

        val groupedBy = bodyPartsValue.groupBy { it.date.month }
        item {
            Text(
                text = "History",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                textDecoration = TextDecoration.Underline
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        groupedBy.forEach{ (month, bodyPartValues) ->
            stickyHeader {
                Text(
                    text = month.name,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            items(bodyPartValues){
                HistoryCard(
                    modifier = Modifier.padding(bottom = 8.dp),
                    bodyPartsValue = it,
                    measuringUnitCode = measuringUnitCode ?: "",
                    onDeleteIconClick = { onDeleteClick(it) }
                )
            }
        }

    }

}

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    bodyPartsValue: BodyPartsValue,
    measuringUnitCode: String?,
    onDeleteIconClick: () -> Unit
) {

    ElevatedCard(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 5.dp)
                    .size(20.dp),
                painter = painterResource(id = R.drawable.img_2),
                contentDescription = "calender icon"
            )
            
            Text(
                text = bodyPartsValue.date.changeLocalDateToDateString(),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${bodyPartsValue.value.roundToDecimal(4)} ${measuringUnitCode ?: ""} ",
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(
                onClick = { onDeleteIconClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete icon"
                )
            }
            
        }

    }

}