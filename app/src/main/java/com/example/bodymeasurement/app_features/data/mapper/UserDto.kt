package com.example.bodymeasurement.app_features.data.mapper

import com.example.bodymeasurement.app_features.domain.model.User

data class UserDto(
    val name: String = "anonymous",
    val email: String = "anonymous@gmail.com",
    val profilePicUrl: String = "",
    val anonymous: Boolean = true,
    val userId: String? = null
)


fun UserDto.toUser(): User{
    return User(
        name = name,
        email = email,
        profilePicUrl = profilePicUrl,
        isAnonymous = anonymous,
        userId = userId
    )
}