package com.example.bodymeasurement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.bodymeasurement.app_features.domain.model.AuthStatus
import com.example.bodymeasurement.app_features.presentation.signIn.SignInViewModel
import com.example.bodymeasurement.core.navigation.Navigation
import com.example.bodymeasurement.core.navigation.Routes
import com.example.bodymeasurement.core.network.ConnectivityPopup
import com.example.bodymeasurement.core.network.NetworkConnectivity
import com.example.bodymeasurement.core.network.NetworkConnectivityImpl
import com.example.bodymeasurement.core.network.NetworkStatus
import com.example.bodymeasurement.ui.theme.BodyMeasurementTheme
import com.example.bodymeasurement.ui.theme.CustomGreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

     private lateinit var networkConnectivity: NetworkConnectivity

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        networkConnectivity = NetworkConnectivityImpl(this)

        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            BodyMeasurementTheme {

                val networkStatus by networkConnectivity.networkStatus().collectAsState(
                    initial = NetworkStatus.Available
                )

                val windowSizeClass = calculateWindowSizeClass(activity = this)
                val navController = rememberNavController()
                val signInViewModel: SignInViewModel = hiltViewModel()

                val authState by signInViewModel.authState.collectAsState()
                var previousAuthStatus by rememberSaveable {
                    mutableStateOf<AuthStatus?>(null)
                }

                LaunchedEffect(key1 = authState) {
                    if (previousAuthStatus != authState){
                        when(authState){
                            AuthStatus.LOADING -> {}
                            AuthStatus.AUTHORISED -> {
                                navController.navigate(Routes.DashboardScreen){ popUpTo(0) }
                            }
                            AuthStatus.UNAUTHORISED -> {
                                navController.navigate(Routes.SignInScreen){ popUpTo(0) }
                            }

                        }
                        previousAuthStatus = authState
                    }
                }

                val snackBarHostState = remember {
                    SnackbarHostState()
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
                ) { innerPadding ->

                    Navigation(
                        navController = navController,
                        paddingValues = innerPadding,
                        windowSize = windowSizeClass.widthSizeClass,
                        sinInViewModel = signInViewModel,
                        snackBarHostState = snackBarHostState
                    )

                    ConnectivityPopup(
                        modifier = Modifier
                            .padding(top = 100.dp),
                        isConnected = networkStatus == NetworkStatus.Available,
                        text = networkStatus.message,
                        backgroundColor = if (networkStatus == NetworkStatus.Available) CustomGreen
                                          else Color.Red,
                        iconRes = painterResource(
                            id = if (networkStatus == NetworkStatus.Available) R.drawable.network_connect
                                   else R.drawable.network_error
                        )
                    )
                    
                }
            }
        }
    }
}




