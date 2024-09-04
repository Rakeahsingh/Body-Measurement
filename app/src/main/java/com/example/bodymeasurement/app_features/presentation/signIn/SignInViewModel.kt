package com.example.bodymeasurement.app_features.presentation.signIn

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodymeasurement.app_features.domain.model.AuthStatus
import com.example.bodymeasurement.app_features.domain.model.predefinedBodyParts
import com.example.bodymeasurement.app_features.domain.repository.AuthRepository
import com.example.bodymeasurement.app_features.domain.repository.DatabaseRepository
import com.example.bodymeasurement.core.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val authState = authRepository.authStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = AuthStatus.LOADING
        )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignInEvent){
        when(event){

            SignInEvent.SignInAnonymously -> {
                signInAnonymously()
            }

            is SignInEvent.SignInWithGoogle -> {
                signInWithGoogle(event.context)
            }

        }
    }

    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update {
                it.copy(isGoogleSignInButtonLoading = true)
            }
            authRepository.signIn(context)
                .onSuccess { isNewUser ->
                    if (isNewUser){
                        databaseRepository.addUser()
                            .onSuccess {
                                try {
                                    insertPredefinedBodyParts()
                                    _uiEvent.send(UiEvent.SnackBar("Signed In Successfully"))

                                }catch (e: Exception){
                                    _uiEvent.send(UiEvent.SnackBar("Signed In, but failed to insert body parts ${e.message}"))

                                }
                            }
                            .onFailure { e ->
                                _uiEvent.send(UiEvent.SnackBar("Couldn't Add User. ${e.message}"))
                            }

                    }else {
                        _uiEvent.send(UiEvent.SnackBar("Signed In Successfully"))
                    }

                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't Sign In.."))
                }

            _state.update {
                it.copy(isGoogleSignInButtonLoading = false)
            }

        }
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            _state.update {
                it.copy(isGoogleSignInButtonLoading = true)
            }
            authRepository.signInAnonymous()
                .onSuccess {
                    databaseRepository.addUser()
                        .onSuccess {
                            try {
                                insertPredefinedBodyParts()
                                _uiEvent.send(UiEvent.SnackBar("Signed In Successfully"))
                            }catch (e: Exception){
                                _uiEvent.send(UiEvent.SnackBar("Signed In, but failed to insert body parts ${e.message}"))
                            }
                        }
                        .onFailure { e ->
                            _uiEvent.send(UiEvent.SnackBar("Couldn't Add User. ${e.message}"))
                        }
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't Signed In. ${e.message}"))
                }

            _state.update {
                it.copy(isAnonymousSighInButtonLoading = false)
            }

        }
    }

    private suspend fun insertPredefinedBodyParts(){
        predefinedBodyParts.forEach { bodyPart ->
            databaseRepository.upsertBodyPart(bodyPart)
        }
    }

}