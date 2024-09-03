package com.example.bodymeasurement.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bodymeasurement.app_features.presentation.addScreen.AddScreen
import com.example.bodymeasurement.app_features.presentation.dashBoardScreen.DashBoardScreen
import com.example.bodymeasurement.app_features.presentation.detailsScreen.DetailsScreen
import com.example.bodymeasurement.app_features.presentation.signInScreen.SignInScreen
import com.example.bodymeasurement.app_features.presentation.signInScreen.SignInViewModel
import com.example.bodymeasurement.core.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    windowSize: WindowWidthSizeClass,
    sinInViewModel: SignInViewModel
) {

    LaunchedEffect(key1 = Unit) {
        sinInViewModel.uiEvent.collectLatest { event ->
            when(event){
                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
                is UiEvent.SnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.DashBoardScreen.route
    ) {


        composable(Screen.SignInScreen.route){
            val state by sinInViewModel.state.collectAsState()
            SignInScreen(
                paddingValues = paddingValues,
                windowSize = windowSize,
                state = state,
                onEvent = sinInViewModel::onEvent
            )
        }

        composable(Screen.DashBoardScreen.route){
            DashBoardScreen()
        }

        composable(Screen.AddScreen.route){
            AddScreen()
        }

        composable(Screen.DetailsScreen.route){
            DetailsScreen()
        }



    }

}