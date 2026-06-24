package com.teamhy2.feature.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.teamhy2.designsystem.common.HY2LoadingScreen
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalNavController
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalShowToast
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.designsystem.util.compositionlocal.ShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.ShowToast
import com.teamhy2.feature.home.navigation.Home
import com.teamhy2.feature.main.component.MainBottomBar
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.tracker.Tracker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

private const val DEFAULT_BACKGROUND_OPACITY = 0.7f

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tracker: Tracker

    private val initialViewModel: InitialViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val versionCode: Long = packageManager.getPackageInfo(packageName, 0).longVersionCode
        initialViewModel.getMinVersion(versionCode)

        checkLaunchedFromFcm()

        enableEdgeToEdge()

        setContent {
            HY2Theme {
                val navController: NavHostController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route

                val tabRoutes = MainTab.entries.map { it.route }.toSet()
                val showBottomBar = currentDestination in tabRoutes

                val scope = rememberCoroutineScope()
                val snackBarHostState = remember { SnackbarHostState() }

                val showSnackBar = initLocalShowSnackBar(scope, snackBarHostState)
                val showToast = initLocalShowToast()

                Scaffold(
                    modifier =
                        Modifier.windowInsetsPadding(
                            WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
                        ),
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    bottomBar = {
                        if (showBottomBar) {
                            MainBottomBar(
                                currentTab = MainTab.fromRoute(currentDestination),
                                onTabSelected = { tab ->
                                    navController.navigate(tab.route) {
                                        popUpTo(Home.ROUTE) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            )
                        }
                    },
                ) { innerPadding ->
                    val postNotificationPermission =
                        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                    val initialUiState by initialViewModel.collectAsState()

                    LaunchedEffect(true) {
                        if (postNotificationPermission.status.isGranted.not()) {
                            postNotificationPermission.launchPermissionRequest()
                        }
                    }

                    Column(
                        modifier =
                            Modifier
                                .paint(
                                    painter = painterResource(id = R.drawable.backgroud),
                                    contentScale = ContentScale.FillBounds,
                                    alpha = DEFAULT_BACKGROUND_OPACITY,
                                )
                                .padding(innerPadding),
                    ) {
                        when (initialUiState) {
                            is InitialState.Loading -> {
                                HY2LoadingScreen()
                            }

                            is InitialState.Success -> {
                                val success = initialUiState as InitialState.Success

                                CompositionLocalProvider(
                                    LocalTracker provides tracker,
                                    LocalShowSnackBar provides showSnackBar,
                                    LocalNavController provides navController,
                                    LocalShowToast provides showToast,
                                ) {
                                    initialViewModel.collectSideEffect { sideEffect ->
                                        when (sideEffect) {
                                            is InitialSideEffect.ShowError ->
                                                showSnackBar.showSnackBar(sideEffect.throwable.message)
                                        }
                                    }

                                    HY2NavHost(
                                        navController = navController,
                                        urls = success.urls,
                                        startDestination = success.startDestination,
                                        onLogoutOrWithdrawComplete = ::restartMainActivity,
                                    )
                                }
                            }

                            is InitialState.NeedUpdate -> {
                                NeedUpdateScreen(
                                    onExitClick = ::finish,
                                    onUpdateClick = ::moveToPlayStoreForUpdate,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkLaunchedFromFcm() {
        val fromFcm = intent.extras?.get("google.message_id") != null
        if (fromFcm) {
            tracker.trackEvent("Push Noti")
        }
    }

    @Composable
    private fun initLocalShowSnackBar(
        scope: CoroutineScope,
        snackBarHostState: SnackbarHostState,
    ) = ShowSnackBar { message: String? ->
        scope.launch {
            snackBarHostState.showSnackbar(
                message = message ?: "예기치 못한 오류가 발생하였습니다\n나중에 다시 시도해주세요",
            )
        }
    }

    @Composable
    private fun initLocalShowToast() =
        ShowToast { message: String? ->
            if (!message.isNullOrBlank()) {
                val inflater = LayoutInflater.from(this)
                val layout = inflater.inflate(R.layout.custom_toast, null)
                val textView = layout.findViewById<TextView>(R.id.custom_toast_text)
                textView.text = message

                Toast(this).apply {
                    duration = Toast.LENGTH_SHORT
                    view = layout
                    show()
                }
            }
        }

    private fun moveToPlayStoreForUpdate() {
        startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(PLAY_STORE_URL)
                setPackage("com.android.vending")
            },
        )
    }

    private fun restartMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val PLAY_STORE_URL =
            "http://play.google.com/store/apps/details?id=com.teamhy2.hongikyeolgong2"
    }
}
