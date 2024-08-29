package com.teamhy2.hongikyeolgong2.timer.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.teamhy2.hongikyeolgong2.timer.model.TimerRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultTimerRepository
    @Inject
    constructor() : TimerRepository {
        private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        override suspend fun getStudyRoomHourDuration(): Long {
            val documentSnapshot: DocumentSnapshot =
                firestore.collection(STUDY_ROOM_COLLECTION)
                    .document(STUDY_ROOM_DOCUMENT_ID)
                    .get()
                    .await()

            return documentSnapshot.getLong(HOUR_DURATION_FIELD) ?: DEFAULT_HOUR_DURATION
        }

        companion object {
            private const val STUDY_ROOM_COLLECTION = "StudyRoom"
            private const val STUDY_ROOM_DOCUMENT_ID = "StudyRoomDuration"
            private const val HOUR_DURATION_FIELD = "hourDuration"
            private const val DEFAULT_HOUR_DURATION: Long = 6
        }
    }
