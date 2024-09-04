package com.example.bodymeasurement.app_features.presentation.dashboard

import com.example.bodymeasurement.app_features.domain.model.BodyPart
import com.example.bodymeasurement.app_features.domain.model.User

data class DashBoardState(
    val user: User? = null,
    val bodyPart: List<BodyPart> = emptyList(),
    val isGoogleSignOutLoadingButton: Boolean = false,
    val isGoogleSignInLoadingButton: Boolean = false
)
