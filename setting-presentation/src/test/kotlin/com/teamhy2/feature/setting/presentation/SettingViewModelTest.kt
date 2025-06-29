package com.teamhy2.feature.setting.presentation

import app.cash.turbine.test
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import com.teamhy2.testing.MainDispatcherRule
import com.teamhy2.user.domain.model.UserInfo
import com.teamhy2.user.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
            coVerify(exactly = 1) { userRepository.getUserInfo() }
            coVerify(exactly = 1) { settingsRepository.notificationSwitchState }
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
                coVerify(exactly = 1) { userRepository.getUserInfo() }
                coVerify(exactly = 0) { settingsRepository.notificationSwitchState }

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
            coVerify(exactly = 1) { userRepository.signOut() }

            // then
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
            coVerify(exactly = 1) { userRepository.signOut() }

            // then
            viewModel.sideEffect.test {
                val actual: SettingSideEffect = awaitItem()
                assertEquals(expected = SettingSideEffect.ShowSnackBar(throwable), actual = actual)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `회원탈퇴 요청이 성공적이라면 State는 Expired로 변경된다`() =
        runTest {
            // given
            coEvery { userRepository.withdraw() } returns Result.success(Unit)
            viewModel = SettingViewModel(settingsRepository, userRepository)

            // when
            viewModel.sendIntent(SettingUiIntent.Withdraw)
            coVerify(exactly = 1) { userRepository.withdraw() }

            // then
            viewModel.uiState.test {
                val actual: SettingUiState = awaitItem()
                assertEquals(expected = SettingUiState.Expired, actual = actual)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `회원탈퇴 요청이 실패했다면 에러 안내 스낵바를 표시한다`() =
        runTest {
            // given
            val throwable = UnknownHostException()
            coEvery { userRepository.withdraw() } returns Result.failure(throwable)
            viewModel = SettingViewModel(settingsRepository, userRepository)

            // when
            viewModel.sendIntent(SettingUiIntent.Withdraw)
            coVerify(exactly = 1) { userRepository.withdraw() }

            // then
            viewModel.sideEffect.test {
                val actual: SettingSideEffect = awaitItem()
                assertEquals(expected = SettingSideEffect.ShowSnackBar(throwable), actual = actual)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `알림 스위치 상태 변경 요청을 할 수 있다`() =
        runTest {
            // given

            coEvery { settingsRepository.saveNotificationSwitchState(true) } just runs
            viewModel = SettingViewModel(settingsRepository, userRepository)

            // when
            viewModel.sendIntent(SettingUiIntent.UpdateNotificationSwitchState(true))
            coVerify(exactly = 1) { settingsRepository.saveNotificationSwitchState(true) }
        }
}
