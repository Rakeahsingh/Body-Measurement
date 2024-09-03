package com.example.bodymeasurement.app_features.domain.repository

import android.content.Context
import com.example.bodymeasurement.app_features.domain.model.AuthStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authStatus: Flow<AuthStatus>

    suspend fun signInAnonymous(): Result<Boolean>

    suspend fun signIn(context: Context): Result<Boolean>

    suspend fun anonymousUserSignInWithGoogle(context: Context): Result<Boolean>

    suspend fun signOut(): Result<Boolean>



}