package com.example.bodymeasurement.app_features.domain.model

data class User(
    val name: String,
    val email: String,
    val profilePicUrl: String,
    val isAnonymous: Boolean,
    val userId: String? = null
)
