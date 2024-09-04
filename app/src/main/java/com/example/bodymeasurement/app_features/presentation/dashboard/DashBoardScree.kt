package com.example.bodymeasurement.app_features.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bodymeasurement.app_features.presentation.components.DashBoardTopBar
import com.example.bodymeasurement.app_features.presentation.components.ItemCard
import com.example.bodymeasurement.app_features.presentation.components.MeasurementDialog
import com.example.bodymeasurement.app_features.presentation.components.ProfileBottomSheet
import com.example.bodymeasurement.core.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    state: DashBoardState,
    onEvent: (DashBoardEvent) -> Unit,
    viewModel: DashBoardViewModel,
    snackBarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    onFabClick: () -> Unit,
    onItemCardClick: (String) -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState()
    var isProfilePicBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when(event){
                UiEvent.HideBottomSheet -> {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) isProfilePicBottomSheetOpen = false
                    }
                }
                UiEvent.Navigate -> {}
                is UiEvent.SnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }


    var isSignOutDialogOpen by remember {
        mutableStateOf(false)
    }
    MeasurementDialog(
        isOpen = isSignOutDialogOpen,
        title = "Sign Out",
        body = {
            Text(text = "You are sure, You want to Sign out?..")
        },
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "warning icon",
                tint = Color.Red,
                modifier = Modifier.size(36.dp)
            )
        },
        onConfirmButtonClick = {
            onEvent(DashBoardEvent.SignOut)
            isSignOutDialogOpen = false
        },
        onDismissButtonClick = {
            isSignOutDialogOpen = false
        }
    )

    val isUserAnonymous = state.user?.isAnonymous ?: true
    ProfileBottomSheet(
        user = state.user,
        isOpen = isProfilePicBottomSheetOpen,
        sheetState = bottomSheetState,
        buttonLoadingState = if (isUserAnonymous) state.isGoogleSignInLoadingButton else state.isGoogleSignOutLoadingButton,
        buttonPrimaryText = if (isUserAnonymous) "SignIn With Google" else "SignOut With Google",
        onGoogleButtonClick = {
            if (isUserAnonymous) onEvent(DashBoardEvent.AnonymousUserSignInWithGoogle(context))
            else isSignOutDialogOpen = true
        },
        onBottomSheetDismiss = { isProfilePicBottomSheetOpen = false }
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            DashBoardTopBar(
                profilePicUrl = state.user?.profilePicUrl,
                onProfilePicClick = {
                    isProfilePicBottomSheetOpen = true
                }
            )

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {

                items(state.bodyPart){ bodyParts ->
                    ItemCard(
                        bodyPart = bodyParts,
                        onItemClick = onItemCardClick
                    )
                }

            }

        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = { onFabClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Icon"
            )
        }

    }


}