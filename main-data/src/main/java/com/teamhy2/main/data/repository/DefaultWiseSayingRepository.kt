package com.teamhy2.main.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.teamhy2.main.domain.model.WiseSaying
import com.teamhy2.main.domain.repository.WiseSayingRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

class DefaultWiseSayingRepository
    @Inject
    constructor() : WiseSayingRepository {
        private val firestore = FirebaseFirestore.getInstance()

        override suspend fun fetchWiseSaying(): WiseSaying {
            val result: QuerySnapshot = firestore.collection(WISE_SAYINGS_COLLECTION).get().await()
            if (result.isEmpty) return WiseSaying("", "")

            val randomIndex = Random.nextInt(result.size())
            val document = result.documents[randomIndex]

            val quote = document.getString(WISE_SAYINGS_QUOTE_FIELD) ?: ""
            val author = document.getString(WISE_SAYINGS_AUTHOR_FIELD) ?: ""

            return WiseSaying(quote, author)
        }

        companion object {
            private const val WISE_SAYINGS_COLLECTION = "WiseSaying"
            private const val WISE_SAYINGS_QUOTE_FIELD = "quote"
            private const val WISE_SAYINGS_AUTHOR_FIELD = "author"
        }
    }
