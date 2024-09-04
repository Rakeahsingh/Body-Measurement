package com.example.bodymeasurement.app_features.presentation.dashboard

import android.content.Context

sealed class DashBoardEvent {

    data class AnonymousUserSignInWithGoogle(val context: Context): DashBoardEvent()

    data object SignOut: DashBoardEvent()

}