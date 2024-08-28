package com.teamhy2.main.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.teamhy2.main.domain.StudyDayRepository
import com.teamhy2.main.model.StudyDay
import com.teamhy2.main.model.toMap
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class DefaultStudyDayRepository
    @Inject
    constructor() : StudyDayRepository {
        private val firestore = FirebaseFirestore.getInstance()

        override suspend fun addStudyDay(
            uid: String,
            startTime: String,
        ) {
            val now = LocalDateTime.now()
            var startTimeParsed = LocalDateTime.of(now.toLocalDate(), LocalTime.parse(startTime))

            if (startTimeParsed.isAfter(now)) {
                startTimeParsed = startTimeParsed.minusDays(1)
            }

            val duration = ChronoUnit.MINUTES.between(startTimeParsed, now)

            val studyDay =
                StudyDay(
                    date = LocalDate.now(),
                    minuteDuration = duration,
                )

            val userDocRef = firestore.collection(USERS_COLLECTION).document(uid)
            val studyDayCollection = userDocRef.collection(STUDYDAY_COLLECTION)
            studyDayCollection.add(studyDay.toMap()).await()
        }

        companion object {
            private const val USERS_COLLECTION = "User"
            private const val STUDYDAY_COLLECTION = "StudyDay"
        }
    }
