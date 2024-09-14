package com.example.bodymeasurement.app_features.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    modifier: Modifier = Modifier,
    user: User?,
    isOpen: Boolean,
    sheetState: SheetState,
    buttonLoadingState: Boolean,
    buttonPrimaryText: String,
    onGoogleButtonClick: () -> Unit,
    onBottomSheetDismiss: () -> Unit
) {

    if (isOpen){
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = { onBottomSheetDismiss() },
            dragHandle = {
                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "User Profile",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    HorizontalDivider()
                }
            }
        ) {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfilePicHolder(
                    padding = 5.dp,
                    borderWidth = 3.dp,
                    placeHolderSize = 120.dp,
                    profilePicUrl = user?.profilePicUrl
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = if (user == null || user.isAnonymous) "Anonymous User" else user.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = if (user == null || user.isAnonymous) "anonymous@user.io.com" else user.email,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.size(20.dp))

                GoogleSignInButton(
                    loadingState = buttonLoadingState,
                    primaryText = buttonPrimaryText,
                    onClick = onGoogleButtonClick
                )

            }

        }
    }

}