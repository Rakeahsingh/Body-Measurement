package com.example.bodymeasurement.app_features.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.bodymeasurement.app_features.domain.model.BodyPart
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.app_features.domain.model.TimeRange
import com.example.bodymeasurement.app_features.domain.repository.DatabaseRepository
import com.example.bodymeasurement.core.navigation.Routes
import com.example.bodymeasurement.core.utils.UiEvent
import com.example.bodymeasurement.core.utils.changeMillisToLocalDate
import com.example.bodymeasurement.core.utils.roundToDecimal
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
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val bodyPartId = savedStateHandle.toRoute<Routes.DetailsScreen>().bodyPartId

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(DetailsState())
    val state = combine(
        _state,
        databaseRepository.getBodyPart(bodyPartId),
        databaseRepository.getAllBodyPartValue(bodyPartId)
    ){ state, bodyPart, bodyPartValues ->
        val currentDate = LocalDate.now()
        val last7Days = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(7))
        }
        val last30Days = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(30))
        }

        state.copy(
            bodyPart = bodyPart,
            allBodyPartValues = bodyPartValues,
            graphBodyPartValues = when(state.timeRange){
                TimeRange.LAST7DAYS -> last7Days
                TimeRange.LAST30DAYS -> last30Days
                TimeRange.ALL_TIME -> bodyPartValues
            }
        )

    }.catch { e ->
        _uiEvent.send(UiEvent.SnackBar("Something went wrong. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DetailsState()
    )


    fun onEvent(event: DetailsEvent){
        when(event){

            DetailsEvent.AddNewValue -> {
                val state = _state.value
                val id = state.allBodyPartValues.find { it.date == state.date }?.bodyPartValueId
                val bodyPartValue = BodyPartsValue(
                    value = state.textFieldValue.roundToDecimal(decimalPlaces = 2),
                    date = state.date,
                    bodyPartId = bodyPartId,
                    bodyPartValueId = id
                )
                upsertBodyPartValue(bodyPartValue)
                _state.update { it.copy(textFieldValue = "") }
            }

            is DetailsEvent.ChangeMeasuringUnit -> {
                val bodyPart = _state.value.bodyPart?.copy(
                    measuringUnit = event.measuringUnit.code
                )
                upsertBodyPart(bodyPart)
            }

            DetailsEvent.DeleteBodyPart -> {
                deleteBodyPart()
            }

            is DetailsEvent.DeleteBodyPartValue -> {
                deleteBodyPartValue(event.bodyPartsValue)
                _state.update {
                    it.copy(recentDeletedBodyPartValue = event.bodyPartsValue)
                }
            }

            is DetailsEvent.OnDateChange -> {
//                val date = event.millis.changeMillisToLocalDate()
                _state.update {
                    it.copy(date = event.millis.changeMillisToLocalDate())
                }
            }

            is DetailsEvent.OnTextFieldValueChange -> {
                _state.update {
                    it.copy(textFieldValue = event.value)
                }
            }

            is DetailsEvent.OnTimeRangeChange -> {
                _state.update {
                    it.copy(timeRange = event.timeRange)
                }
            }

            DetailsEvent.RestoreBodyPartValue -> {
                upsertBodyPartValue(_state.value.recentDeletedBodyPartValue)
                _state.update { it.copy(recentDeletedBodyPartValue = null) }
            }

        }
    }

    private fun upsertBodyPart(bodyPart: BodyPart?) {
        viewModelScope.launch {
            bodyPart ?: return@launch
            databaseRepository.upsertBodyPart(bodyPart)
                .onSuccess {
                    _uiEvent.send(UiEvent.SnackBar("Body part Saved Successfully."))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't save body part. ${e.message}"))
                }
        }
    }

    private fun deleteBodyPartValue(bodyPartsValue: BodyPartsValue) {
        viewModelScope.launch {
            databaseRepository.deleteBodyPartValue(bodyPartsValue)
                .onSuccess {
                    _uiEvent.send(UiEvent.SnackBar("Body Part Value Deleted Successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't delete body part value. ${e.message}"))
                }
        }
    }

    private fun deleteBodyPart() {
        viewModelScope.launch {
            databaseRepository.deleteBodyPart(bodyPartId)
                .onSuccess {
                    _uiEvent.send(UiEvent.Navigate)
                    _uiEvent.send(UiEvent.SnackBar("Body Part Deleted Successfully."))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't Delete Body Part ${e.message}"))
                }
        }
    }

    private fun upsertBodyPartValue(bodyPartValue: BodyPartsValue?) {
        viewModelScope.launch {
            bodyPartValue ?: return@launch
            databaseRepository.upsertBodyPartValue(bodyPartValue)
                .onSuccess {
                    _uiEvent.send(UiEvent.SnackBar("Body Part Value Saved Successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't save Body Part. ${e.message}"))
                }
        }
    }

}