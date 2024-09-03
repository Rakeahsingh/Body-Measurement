package com.example.bodymeasurement.core.navigation

sealed class Screen(val route: String) {

    data object SignInScreen: Screen("signIn_screen")

    data object DashBoardScreen: Screen("home_screen")

    data object AddScreen: Screen("add_screen")

    data object DetailsScreen: Screen("details_screen")

}