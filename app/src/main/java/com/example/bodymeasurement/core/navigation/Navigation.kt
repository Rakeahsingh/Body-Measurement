package com.example.bodymeasurement.core.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bodymeasurement.app_features.presentation.addItem.AddItemScreen
import com.example.bodymeasurement.app_features.presentation.addItem.AddItemViewModel
import com.example.bodymeasurement.app_features.presentation.dashboard.DashBoardScreen
import com.example.bodymeasurement.app_features.presentation.dashboard.DashBoardViewModel
import com.example.bodymeasurement.app_features.presentation.details.DetailsScreen
import com.example.bodymeasurement.app_features.presentation.signIn.SignInScreen
import com.example.bodymeasurement.app_features.presentation.signIn.SignInViewModel
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
        startDestination = Routes.DashboardScreen,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {


        composable<Routes.SignInScreen> {
            val state by sinInViewModel.state.collectAsState()
            SignInScreen(
                windowSize = windowSize,
                paddingValues = paddingValues,
                state = state,
                onEvent = sinInViewModel::onEvent
            )
        }

        composable<Routes.DashboardScreen>{
            val viewModel: DashBoardViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            DashBoardScreen(
                state = state,
                onEvent = viewModel::onEvent,
                viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                paddingValues = paddingValues,
                onFabClick = {
                    navController.navigate(Routes.AddItemScreen)
                },
                onItemCardClick = { bodyPartId ->
                    navController.navigate(Routes.DetailsScreen(bodyPartId))
                }
            )
        }

        composable<Routes.AddItemScreen>{
            val viewModel: AddItemViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            AddItemScreen(
                snackBarHostState = snackBarHostState,
                paddingValues = paddingValues,
                state = state,
                onEvent = viewModel::onEvent,
                viewModel = viewModel,
                onBackIconClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<Routes.DetailsScreen>{
            DetailsScreen()
        }


    }

}