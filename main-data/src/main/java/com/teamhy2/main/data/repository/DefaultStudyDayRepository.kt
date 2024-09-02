package com.teamhy2.main.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.teamhy2.main.domain.model.StudyDayResponse
import com.teamhy2.main.domain.repository.StudyDayRepository
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

            val duration: Long = ChronoUnit.SECONDS.between(startTimeParsed, now)

            val yearMonthFormatter = DateTimeFormatter.ofPattern(YEAR_MONTH_PATTERN)
            val yearMonth = now.format(yearMonthFormatter)
            val dayOfMonth = now.dayOfMonth.toString()

            val userDocumentReference: DocumentReference =
                firestore.collection(USERS_COLLECTION).document(uid)
            val studyDayDocument: DocumentReference =
                userDocumentReference.collection(STUDYDAY_COLLECTION).document(yearMonth)
            val dayDocument: DocumentReference =
                studyDayDocument.collection(DAY_COLLECTION).document(dayOfMonth)

            firestore.runTransaction { transaction ->
                val daySnapshot = transaction.get(dayDocument)
                val studyStartTimeList =
                    daySnapshot.get(STUDY_START_TIME_FIELD) as? List<String> ?: emptyList()
                val studySecondDurationList =
                    daySnapshot.get(STUDY_SECOND_DURATION_FIELD) as? List<Long> ?: emptyList()

                val monthSnapshot = transaction.get(studyDayDocument)
                val currentMonthTotal = monthSnapshot.getLong(TOTAL_MONTH_STUDY_TIME_FIELD) ?: 0L

                val userSnapshot = transaction.get(userDocumentReference)
                val currentTotalStudyTime = userSnapshot.getLong(TOTAL_STUDY_TIME_FIELD) ?: 0L

                // Day 문서에 대한 업데이트 (존재하지 않으면 새로 생성)
                val updatedDayData = mutableMapOf<String, Any>()
                updatedDayData[STUDY_START_TIME_FIELD] =
                    studyStartTimeList + startTimeParsed.toLocalTime().toString()
                updatedDayData[STUDY_SECOND_DURATION_FIELD] = studySecondDurationList + duration
                transaction.set(dayDocument, updatedDayData)

                // Month 문서에 대한 업데이트 (존재하지 않으면 새로 생성)
                val newMonthTotal = currentMonthTotal + duration
                transaction.set(
                    studyDayDocument,
                    mapOf(TOTAL_MONTH_STUDY_TIME_FIELD to newMonthTotal),
                    SetOptions.merge(),
                )

                // User 문서에 대한 업데이트 (존재하지 않으면 새로 생성)
                val newTotalStudyTime = currentTotalStudyTime + duration
                transaction.set(
                    userDocumentReference,
                    mapOf(TOTAL_STUDY_TIME_FIELD to newTotalStudyTime),
                    SetOptions.merge(),
                )
            }.await()
        }

        override suspend fun getStudyDaysByMonth(uid: String): Map<String, List<StudyDayResponse>> {
            val userDocumentReference = firestore.collection(USERS_COLLECTION).document(uid)
            val studyDayCollectionReference = userDocumentReference.collection(STUDYDAY_COLLECTION)

            val studyDaysByMonthResponse = mutableMapOf<String, List<StudyDayResponse>>()
            val studyDayDocuments = studyDayCollectionReference.get().await().documents

            studyDayDocuments.forEach { yearMonthDoc ->
                val yearMonth = yearMonthDoc.id
                val daysCollection = yearMonthDoc.reference.collection(DAY_COLLECTION).get().await()

                val studyDayResponses =
                    daysCollection.documents.map { dayDoc ->
                        val day = dayDoc.id.toInt()
                        val date =
                            LocalDate.of(
                                yearMonth.split("-")[0].toInt(),
                                yearMonth.split("-")[1].toInt(),
                                day,
                            )
                        val studyStartTimes = dayDoc.get(STUDY_START_TIME_FIELD) as List<String>
                        StudyDayResponse(date, studyStartTimes)
                    }

                studyDaysByMonthResponse[yearMonth] = studyDayResponses
            }

            return studyDaysByMonthResponse
        }

        companion object {
            private const val USERS_COLLECTION = "User"
            private const val STUDYDAY_COLLECTION = "StudyDay"
            private const val DAY_COLLECTION = "Day"
            private const val YEAR_MONTH_PATTERN = "yyyy-MM"
            private const val STUDY_START_TIME_FIELD = "studyStartTime"
            private const val STUDY_SECOND_DURATION_FIELD = "studySecondDuration"
            private const val TOTAL_MONTH_STUDY_TIME_FIELD = "totalMonthStudyTime"
            private const val TOTAL_STUDY_TIME_FIELD = "totalStudyTime"
        }
    }
