package com.example.bodymeasurement.app_features.domain.repository

import com.example.bodymeasurement.app_features.domain.model.BodyPart
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.app_features.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun getSignedInUsers(): Flow<User?>

    suspend fun addUser(): Result<Boolean>

    fun getBodyPart(bodyPartId: String): Flow<BodyPart?>

    fun getAllBodyParts(): Flow<List<BodyPart>>

    fun getAllBodyPartsWithLatestValues(): Flow<List<BodyPart>>

    fun getAllBodyPartValue(bodyPartId: String): Flow<List<BodyPartsValue>>

    suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean>

    suspend fun deleteBodyPart(bodyPartId: String): Result<Boolean>

    suspend fun upsertBodyPartValue(bodyPartsValue: BodyPartsValue): Result<Boolean>

    suspend fun deleteBodyPartValue(bodyPartsValue: BodyPartsValue): Result<Boolean>

}