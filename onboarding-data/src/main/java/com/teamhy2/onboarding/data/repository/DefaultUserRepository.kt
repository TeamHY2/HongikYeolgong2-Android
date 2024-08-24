package com.teamhy2.onboarding.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.teamhy2.onboarding.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultUserRepository
    @Inject
    constructor() : UserRepository {
        private val firestore = FirebaseFirestore.getInstance()

        override suspend fun checkNicknameDuplication(nickname: String): Boolean {
            try {
                val result: QuerySnapshot = firestore.collection(FIREBASE_USER_COLLECTION).get().await()
                result.documents.find { it.getString(FIREBASE_NICKNAME_DOCUMENT_ID) == nickname }
                    ?.let { return true }
            } catch (e: Exception) {
                Log.d("DefaultRepository", "Error ", e)
            }

            return false
        }

        override suspend fun signUp(
            nickname: String,
            department: String,
        ) {
            val user =
                hashMapOf(
                    FIREBASE_NICKNAME_DOCUMENT_ID to nickname,
                    "department" to department,
                )

            firestore.collection(FIREBASE_USER_COLLECTION)
                .document(Firebase.auth.currentUser?.uid!!)
                .set(user)
        }

        companion object {
            private const val FIREBASE_USER_COLLECTION = "User"
            private const val FIREBASE_NICKNAME_DOCUMENT_ID = "nickname"
        }
    }
