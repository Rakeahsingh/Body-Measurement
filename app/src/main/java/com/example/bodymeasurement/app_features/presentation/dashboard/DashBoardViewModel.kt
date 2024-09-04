package com.example.bodymeasurement.app_features.presentation.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodymeasurement.app_features.domain.repository.AuthRepository
import com.example.bodymeasurement.app_features.domain.repository.DatabaseRepository
import com.example.bodymeasurement.core.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
): ViewModel() {



    private val _state = MutableStateFlow(DashBoardState())
    val state = combine(
        _state,
        databaseRepository.getSignedInUsers(),
        databaseRepository.getAllBodyPartsWithLatestValues()
    ){ state, user, bodyParts ->
        val activeBodyParts = bodyParts.filter { it.isActive }
        state.copy(
            user = user,
            bodyPart = activeBodyParts
        )
    }.catch { e ->
        _uiEvent.send(UiEvent.SnackBar("Something went wrong ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashBoardState()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: DashBoardEvent){
        when(event){

            is DashBoardEvent.AnonymousUserSignInWithGoogle -> {
                anonymousUserSignInWithGoogle(event.context)
            }

            DashBoardEvent.SignOut -> {
                signOut()
            }

        }
    }

    private fun anonymousUserSignInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update {
                it.copy(isGoogleSignInLoadingButton = true)
            }

            authRepository.anonymousUserSignInWithGoogle(context)
                .onSuccess {
                    databaseRepository.addUser()
                        .onSuccess {
                            _uiEvent.send(UiEvent.HideBottomSheet)
                            _uiEvent.send(UiEvent.SnackBar("SignIn Successfully"))
                        }
                        .onFailure { e ->
                            _uiEvent.send(UiEvent.HideBottomSheet)
                            _uiEvent.send(UiEvent.SnackBar("Couldn't Add User. ${e.message}"))
                        }
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(UiEvent.SnackBar("Couldn't SignIn. ${e.message}"))
                }

            _state.update {
                it.copy(isGoogleSignInLoadingButton = false)
            }

        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _state.update {
                it.copy(isGoogleSignOutLoadingButton = true)
            }

            authRepository.signOut()
                .onSuccess {
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(UiEvent.SnackBar("SignOut Successfully"))
                }

                .onFailure { e ->
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(UiEvent.SnackBar("Couldn't Sign Out. ${e.message}"))
                }

            _state.update {
                it.copy(isGoogleSignOutLoadingButton = false)
            }

        }
    }


}