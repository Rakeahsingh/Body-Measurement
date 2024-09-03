package com.example.bodymeasurement.app_features.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bodymeasurement.app_features.data.mapper.BodyPartDto
import com.example.bodymeasurement.app_features.data.mapper.BodyPartsValueDto
import com.example.bodymeasurement.app_features.data.mapper.UserDto
import com.example.bodymeasurement.app_features.data.mapper.toBodyPart
import com.example.bodymeasurement.app_features.data.mapper.toBodyPartDto
import com.example.bodymeasurement.app_features.data.mapper.toBodyPartValue
import com.example.bodymeasurement.app_features.data.mapper.toBodyPartValueDto
import com.example.bodymeasurement.app_features.data.mapper.toUser
import com.example.bodymeasurement.app_features.domain.model.BodyPart
import com.example.bodymeasurement.app_features.domain.model.BodyPartsValue
import com.example.bodymeasurement.app_features.domain.model.User
import com.example.bodymeasurement.app_features.domain.repository.DatabaseRepository
import com.example.bodymeasurement.core.utils.Constants.BODY_PART_COLLECTION
import com.example.bodymeasurement.core.utils.Constants.BODY_PART_NAME_FIELD
import com.example.bodymeasurement.core.utils.Constants.BODY_PART_VALUE_COLLECTION
import com.example.bodymeasurement.core.utils.Constants.BODY_PART_VALUE_DATE_FIELD
import com.example.bodymeasurement.core.utils.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): DatabaseRepository {

    override fun getSignedInUsers(): Flow<User?> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                userCollection()
                    .document(userId)
                    .snapshots()
                    .collect{ snapshot ->
                        val userDto = snapshot.toObject(UserDto::class.java)
                        emit(userDto?.toUser())
                    }

            }catch (e: Exception){
                throw e
            }
        }
    }

    override suspend fun addUser(): Result<Boolean> {
        return try {
            val firebaseUser = firebaseAuth.currentUser ?: throw IllegalArgumentException("No current user found")

            var userDto = UserDto(
                userId = firebaseUser.uid,
                anonymous = firebaseUser.isAnonymous
            )

            firebaseUser.providerData.forEach { profile ->
                userDto = userDto.copy(
                    name = profile.displayName ?: userDto.name,
                    email = profile.email ?: userDto.email,
                    profilePicUrl = profile.photoUrl.toString() ?: userDto.profilePicUrl
                )
            }

            userCollection()
                .document(firebaseUser.uid)
                .set(userDto)
                .await()
            Result.success(value = true)

        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override fun getBodyPart(bodyPartId: String): Flow<BodyPart?> {
        return flow {
            try {
                bodyPartCollection()
                    .document(bodyPartId)
                    .snapshots()
                    .collect{ snapshot ->
                        val bodyPartDto = snapshot.toObject(BodyPartDto::class.java)
                        emit(bodyPartDto?.toBodyPart())
                    }

            }catch (e:Exception){
                throw e
            }
        }
    }

    override fun getAllBodyParts(): Flow<List<BodyPart>> {
        return flow {
            try {
                bodyPartCollection()
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect{ snapshot ->
                        val bodyPartDto = snapshot.toObjects(BodyPartDto::class.java)
                        emit(bodyPartDto.map { it.toBodyPart() })
                    }

            }catch (e: Exception){
                throw e
            }
        }
    }

    override fun getAllBodyPartsWithLatestValues(): Flow<List<BodyPart>> {
        return flow {
            try {
                bodyPartCollection()
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect{ snapshot ->
                        val bodyPartsDto = snapshot.toObjects(BodyPartDto::class.java)
                        val bodyParts = bodyPartsDto.mapNotNull { bodyPartDto ->
                            bodyPartDto.bodyPartId?.let { bodyPartId ->
                                val latestValue = getLatestBodyPartValue(bodyPartId)
                                bodyPartDto.copy(latestValue = latestValue?.value).toBodyPart()
                            }
                        }
                        emit(bodyParts)
                    }

            }catch (e: Exception){
                throw e
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllBodyPartValue(bodyPartId: String): Flow<List<BodyPartsValue>> {
        return flow {
            try {
                bodyPartValueCollection(bodyPartId)
                    .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
                    .snapshots()
                    .collect{ snapshot ->
                        val bodyPartValueDto = snapshot.toObjects(BodyPartsValueDto::class.java)
                        emit(bodyPartValueDto.map { it.toBodyPartValue() })
                    }

            }catch (e: Exception){
                throw e
            }
        }
    }

    override suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean> {
        return try {
            val documentId = bodyPart.bodyPartId ?: bodyPartCollection().document().id
            val bodyPartDto = bodyPart.toBodyPartDto().copy(bodyPartId = documentId)
            bodyPartCollection()
                .document(documentId)
                .set(bodyPartDto)
                .await()
            Result.success(value = true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteBodyPart(bodyPartId: String): Result<Boolean> {
        return try {
            bodyPartCollection()
                .document(bodyPartId)
                .delete()
                .await()
            Result.success(value = true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun upsertBodyPartValue(bodyPartsValue: BodyPartsValue): Result<Boolean> {
        return try {
            val bodyPartValueCollection = bodyPartValueCollection(bodyPartsValue.bodyPartId.orEmpty())
            val documentId = bodyPartsValue.bodyPartValueId ?: bodyPartValueCollection.document().id
            val bodyPartValueDto = bodyPartsValue.toBodyPartValueDto().copy(bodyPartValueId = documentId)

            bodyPartValueCollection
                .document(documentId)
                .set(bodyPartValueDto)
                .await()
            Result.success(value = true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteBodyPartValue(bodyPartsValue: BodyPartsValue): Result<Boolean> {
        return try {
            val bodyPartId = bodyPartsValue.bodyPartId.orEmpty()
            val bodyPartsValueId = bodyPartsValue.bodyPartValueId.orEmpty()
            bodyPartValueCollection(bodyPartId)
                .document(bodyPartsValueId)
                .delete()
                .await()
            Result.success(value = true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }



    private suspend fun getLatestBodyPartValue(bodyPartId: String): BodyPartsValueDto? {
        val querySnapshot = bodyPartValueCollection(bodyPartId)
            .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
            .limit(1)
            .snapshots()
            .firstOrNull()

        return querySnapshot?.documents?.firstOrNull()?.toObject(BodyPartsValueDto::class.java)
    }


    private fun userCollection(): CollectionReference{
        return firestore
            .collection(USER_COLLECTION)
    }

    private fun bodyPartCollection(
        userId: String = firebaseAuth.currentUser?.uid.orEmpty()
    ): CollectionReference{
        return firestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(BODY_PART_COLLECTION)
    }

    private fun bodyPartValueCollection(
        bodyPartId: String,
        userId: String = firebaseAuth.currentUser?.uid.orEmpty()
    ): CollectionReference{
        return firestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(BODY_PART_COLLECTION)
            .document(bodyPartId)
            .collection(BODY_PART_VALUE_COLLECTION)
    }

}