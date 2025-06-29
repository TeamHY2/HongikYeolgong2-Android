package com.teamhy2.feature.setting

import app.cash.turbine.test
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.feature.setting.presentation.SettingSideEffect
import com.teamhy2.feature.setting.presentation.SettingUiIntent
import com.teamhy2.feature.setting.presentation.SettingUiState
import com.teamhy2.feature.setting.presentation.SettingViewModel
import com.teamhy2.testing.MainDispatcherRule
import com.teamhy2.user.domain.model.UserInfo
import com.teamhy2.user.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertEquals

class SettingViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: SettingViewModel
    private val settingsRepository: SettingsRepository = mockk()
    private val userRepository: UserRepository = mockk()

    @Test
    fun `SettingViewModel의 초기 상태는 Loading이다`() =
        runTest {
            // given & when
            viewModel = SettingViewModel(settingsRepository, userRepository)

            // then
            viewModel.uiState.test {
                val actual: SettingUiState = awaitItem()
                assertEquals(expected = SettingUiState.Loading, actual = actual)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `현재 유저 정보와 알림 설정 유무를 가져올 수 있다`() =
        runTest {
            // given
            val mockUserInfo =
                UserInfo(
                    nickname = "반달",
                    email = "dddd@gmail.com",
                    department = "컴퓨터과학과",
                )
            val mockNotificationSwitchState: Flow<Boolean> = flowOf(true)
            coEvery { userRepository.getUserInfo() } returns Result.success(mockUserInfo)
            coEvery { settingsRepository.notificationSwitchState } returns mockNotificationSwitchState
            viewModel = SettingViewModel(settingsRepository, userRepository)

            viewModel.uiState.test {
                val initState: SettingUiState = awaitItem()
                assertEquals(expected = SettingUiState.Loading, actual = initState)

                // when
                viewModel.initSettingUiState()
                val actual: SettingUiState = awaitItem()

                // then
                assertEquals(
                    expected =
                        SettingUiState.Success(
                            isNotificationSwitchChecked = true,
                            userInfo = mockUserInfo,
                        ),
                    actual = actual,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `현재유저정보를 가져오지 못한다면 State는 Loading을 유지하며 에러 안내 스낵바를 표시한다`() =
        runTest {
            // given
            val throwable = UnknownHostException()
            val mockNotificationSwitchState: Flow<Boolean> = flowOf(true)
            coEvery { userRepository.getUserInfo() } returns Result.failure(throwable)
            coEvery { settingsRepository.notificationSwitchState } returns mockNotificationSwitchState
            viewModel = SettingViewModel(settingsRepository, userRepository)

            viewModel.uiState.test {
                val initState = awaitItem()
                assertEquals(expected = SettingUiState.Loading, actual = initState)

                // when
                viewModel.initSettingUiState()
                val actual: SettingUiState = viewModel.uiState.value

                // then
                assertEquals(expected = SettingUiState.Loading, actual = actual)
            }
            viewModel.sideEffect.test {
                val actual: SettingSideEffect = awaitItem()

                // then
                assertEquals(
                    expected = SettingSideEffect.ShowSnackBar(throwable),
                    actual = actual,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `로그아웃 요청이 성공적이라면 State는 Expired로 변경된다`() =
        runTest {
            // given
            coEvery { userRepository.signOut() } returns Result.success(Unit)
            viewModel = SettingViewModel(settingsRepository, userRepository)

            // when
            viewModel.sendIntent(SettingUiIntent.SignOut)

            // then
            coVerify(exactly = 1) { userRepository.signOut() }
            viewModel.uiState.test {
                val actual: SettingUiState = awaitItem()
                assertEquals(expected = SettingUiState.Expired, actual = actual)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `로그아웃 요청이 실패하였다면 에러 안내 스낵바를 표시한다`() =
        runTest {
            // given
            val throwable = UnknownHostException()
            coEvery { userRepository.signOut() } returns Result.failure(throwable)
            viewModel = SettingViewModel(settingsRepository, userRepository)

            // when
            viewModel.sendIntent(SettingUiIntent.SignOut)

            // then
            coVerify(exactly = 1) { userRepository.signOut() }
            viewModel.sideEffect.test {
                val actual: SettingSideEffect = awaitItem()
                assertEquals(expected = SettingSideEffect.ShowSnackBar(throwable), actual = actual)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
