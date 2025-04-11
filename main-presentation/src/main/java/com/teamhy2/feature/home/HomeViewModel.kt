package com.teamhy2.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.teamhy2.main.domain.model.Promotion
import com.teamhy2.main.domain.model.WeeklyStudyDay
import com.teamhy2.main.domain.repository.PromotionRepository
import com.teamhy2.main.domain.repository.StudyDayRepository
import com.teamhy2.main.domain.repository.WiseSayingRepository
import com.teamhy2.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val wiseSayingRepository: WiseSayingRepository,
        private val studyDayRepository: StudyDayRepository,
        private val promotionRepository: PromotionRepository,
        private val userRepository: UserRepository,
    ) : ViewModel(), ContainerHost<HomeState, HomeSideEffect> {
        override val container: Container<HomeState, HomeSideEffect> = container(HomeState.Loading)

        init {
            loadPromotionData()
            loadHomeData()
            updateDeviceToken()
        }

        private fun loadHomeData() =
            intent {
                runCatching {
                    coroutineScope {
                        val deferredWiseSaying =
                            async { wiseSayingRepository.fetchWiseSaying().getOrThrow() }
                        val deferredWeeklyStudyDays =
                            async { studyDayRepository.fetchWeeklyStudyDay().getOrThrow() }

                        deferredWiseSaying.await() to deferredWeeklyStudyDays.await()
                    }
                }
                    .onSuccess { (wiseSaying, weeklyStudyDays) ->
                        reduce {
                            HomeState.Success(
                                wiseSaying = wiseSaying,
                                weeklyStudyDays = weeklyStudyDays,
                            )
                        }
                    }
                    .onFailure { postSideEffect(HomeSideEffect.ShowError(it)) }
            }

        private fun loadPromotionData() =
            intent {
                runCatching {
                    val promotion: Promotion = promotionRepository.fetchPromotionData().getOrThrow()
                    val isDismissedFlow: Flow<Boolean> = promotionRepository.isPromotionDismissed

                    isDismissedFlow.collect { isDismissed ->
                        runOn<HomeState.Success> {
                            reduce {
                                state.copy(
                                    promotion = promotion,
                                    isPromotionDialog = !isDismissed && promotion.isActive,
                                )
                            }
                        }
                    }
                }.onFailure { postSideEffect(HomeSideEffect.ShowError(it)) }
            }

        fun increaseTodayStudyCount() =
            intent {
                runOn<HomeState.Success> {
                    reduce {
                        val updatedWeeklyStudyDays: List<WeeklyStudyDay> =
                            state.weeklyStudyDays.map { studyDay ->
                                if (studyDay.date ==
                                    LocalDate.now()
                                        .format(DateTimeFormatter.ofPattern("M/dd"))
                                ) {
                                    studyDay.copy(studyCount = studyDay.studyCount + 1)
                                } else {
                                    studyDay
                                }
                            }
                        state.copy(weeklyStudyDays = updatedWeeklyStudyDays)
                    }
                }
            }

        fun updatePromotionDismissPeriod(
            startDate: LocalDate,
            endDate: LocalDate,
        ) = intent {
            promotionRepository.savePromotionDismissPeriod(startDate, endDate)
                .onFailure { postSideEffect(HomeSideEffect.ShowError(it)) }
        }

        fun updatePromotionDialogVisibility(isVisible: Boolean) =
            intent {
                runOn<HomeState.Success> {
                    reduce { state.copy(isPromotionDialog = isVisible) }
                }
            }

        private fun updateDeviceToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful.not()) {
                        return@OnCompleteListener
                    }

                    val token = task.result

                    viewModelScope.launch {
                        userRepository.updateDeviceToken(token)
                            .onSuccess {
                                Log.d(TAG, "Device token updated successfully")
                            }
                            .onFailure { exception ->
                                Log.e(TAG, "Failed to update device token", exception)
                            }
                    }
                },
            )
        }

        companion object {
            private const val TAG = "HomeViewModel"
        }
    }
