package com.teamhy2.onboarding.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.teamhy2.onboarding.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultUserRepository
    @Inject
    constructor() : UserRepository {
        private val firestore = FirebaseFirestore.getInstance()

        override suspend fun checkUserExists(uid: String): Boolean {
            val result: QuerySnapshot = firestore.collection(FIREBASE_USER_COLLECTION).get().await()
            val user: DocumentSnapshot? =
                result.documents.find { it.id == uid }
            user?.let { return true }
            return false
        }

        override suspend fun checkNicknameDuplication(nickname: String): Boolean {
            try {
                val result: QuerySnapshot = firestore.collection(FIREBASE_USER_COLLECTION).get().await()
                result.documents.find { it.getString(NICKNAME_KEY) == nickname }
                    ?.let { return true }
            } catch (e: Exception) {
                Log.d("checkNicknameDuplication", "Error ", e)
            }

            return false
        }

        override suspend fun signUp(
            nickname: String,
            department: String,
        ) {
            val user =
                hashMapOf(
                    NICKNAME_KEY to nickname,
                    DEPARTMENT_KEY to department,
                    EMAIL_KEY to Firebase.auth.currentUser?.email,
                    ID_KEY to Firebase.auth.currentUser?.uid,
                )

            firestore.collection(FIREBASE_USER_COLLECTION)
                .document(Firebase.auth.currentUser?.uid!!)
                .set(user)
        }

        override suspend fun withdraw() {
            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid ?: ""

            firestore.collection(FIREBASE_USER_COLLECTION).document(uid).delete().await()

            user?.delete()?.await()
        }

        companion object {
            private const val FIREBASE_USER_COLLECTION = "User"
            private const val NICKNAME_KEY = "nickname"
            private const val DEPARTMENT_KEY = "department"
            private const val EMAIL_KEY = "email"
            private const val ID_KEY = "id"
        }
    }
