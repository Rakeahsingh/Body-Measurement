package com.example.bodymeasurement.app_features.presentation.signIn

import android.content.Context

sealed class SignInEvent {

    data class SignInWithGoogle(val context: Context): SignInEvent()
    data object SignInAnonymously: SignInEvent()

}