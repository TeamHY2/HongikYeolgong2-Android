package com.teamhy2.onboarding.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultWebViewRepository
    @Inject
    constructor() : WebViewRepository {
        private val firestore = FirebaseFirestore.getInstance()

        override suspend fun fetchFirebaseUrls(): Map<String, String> {
            val urlMap = mutableMapOf<String, String>()

            try {
                val result = firestore.collection(FIREBASE_URLS_COLLECTION).get().await()
                for (document in result) {
                    val url = document.getString(FIREBASE_URLS_DOCUMENT_ID)
                    if (url != null) {
                        urlMap[document.id] = url
                    }
                }
            } catch (e: Exception) {
                Log.d("FirebaseRepositoryImp", "Error ", e)
            }
            return urlMap
        }

        companion object {
            private const val FIREBASE_URLS_COLLECTION = "Link"
            private const val FIREBASE_URLS_DOCUMENT_ID = "url"
        }
    }
