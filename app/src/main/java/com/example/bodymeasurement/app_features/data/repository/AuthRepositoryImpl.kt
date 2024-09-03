package com.example.bodymeasurement.app_features.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.bodymeasurement.app_features.domain.model.AuthStatus
import com.example.bodymeasurement.app_features.domain.repository.AuthRepository
import com.example.bodymeasurement.core.utils.Constants
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager
): AuthRepository {


    override val authStatus: Flow<AuthStatus> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val status = if (auth.currentUser != null){
                AuthStatus.AUTHORISED
            }else AuthStatus.UNAUTHORISED
            trySend(status)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose{
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.distinctUntilChanged()


    override suspend fun signInAnonymous(): Result<Boolean> {
        return try {
            firebaseAuth.signInAnonymously().await()
            Result.success(value = true)

        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signIn(context: Context): Result<Boolean> {
        return try {
            val authCredentialResult = getGoogleAuthCredentials(context)
            if (authCredentialResult.isSuccess){
                val authCredential = authCredentialResult.getOrNull()
                if (authCredential != null){
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

                    Result.success(isNewUser)
                }else {
                    Result.failure(IllegalArgumentException("Auth Credential is null"))
                }

            }else {
                Result.failure(authCredentialResult.exceptionOrNull() ?: Exception("Unknown Error") )
            }

        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun anonymousUserSignInWithGoogle(context: Context): Result<Boolean> {
        return try {
            val authCredentialResult = getGoogleAuthCredentials(context)
            if (authCredentialResult.isSuccess){
                val authCredential = authCredentialResult.getOrNull()
                if (authCredential != null){
                    firebaseAuth.currentUser?.linkWithCredential(authCredential)?.await()

                    Result.success(value = true)

                }else {
                    Result.failure(IllegalArgumentException("Auth Credential is null"))
                }

            }else {
                Result.failure(authCredentialResult.exceptionOrNull() ?: Exception("Unknown Error"))
            }

        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            firebaseAuth.signOut()
            Result.success(value = true)

        }catch (e: Exception){
            Result.failure(e)
        }
    }


    private suspend fun getGoogleAuthCredentials(context: Context): Result<AuthCredential?>{
        return try {
            val nonce = UUID.randomUUID().toString()
            val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(Constants.GOOGLE_CLIENT_ID)
                .setNonce(nonce)
            .build()

            val request : GetCredentialRequest = GetCredentialRequest
                .Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val credential = credentialManager.getCredential(context, request).credential
            val googleIdToken = GoogleIdTokenCredential
                .createFrom(credential.data).idToken
            val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            Result.success(authCredential)

        }catch (e: Exception){
            Result.failure(e)
        }
    }


}