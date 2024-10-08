package com.teamhy2.feature.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.teamhy2.designsystem.common.HY2LoadingScreen
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.hongikyeolgong2.main.presentation.R
import dagger.hilt.android.AndroidEntryPoint

private const val DEFAULT_BACKGROUND_OPACITY = 0.7f

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val signInLauncher =
        registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            this.onSignInResult(res)
        }

    private val initialViewModel: InitialViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val versionCode: Long = packageManager.getPackageInfo(packageName, 0).longVersionCode
        initialViewModel.getMinVersion(versionCode)

        enableEdgeToEdge()
        setContent {
            HY2Theme {
                Scaffold(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                ) { innerPadding ->
                    val postNotificationPermission =
                        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                    val initialUiState by initialViewModel.initialUiState.collectAsStateWithLifecycle()

                    LaunchedEffect(key1 = true) {
                        if (!postNotificationPermission.status.isGranted) {
                            postNotificationPermission.launchPermissionRequest()
                        }
                    }

                    var backgroundState by remember {
                        mutableStateOf(BackgroundState.DEFAULT)
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
                            is InitialUiState.Loading -> {
                                HY2LoadingScreen()
                            }

                            is InitialUiState.Success -> {
                                HY2NavHost(
                                    navController = rememberNavController(),
                                    urls = (initialUiState as InitialUiState.Success).urls,
                                    googleSignIn = ::createSignInIntent,
                                    startDestination = (initialUiState as InitialUiState.Success).startDestination,
                                    onSendNotification = { pushText ->
                                        initialViewModel.notificationHandler.showSimpleNotification(
                                            pushText,
                                        )
                                    },
                                    onLogoutOrWithdrawComplete = {
                                        restartMainActivity()
                                    },
                                )
                            }

                            is InitialUiState.NeedUpdate -> {
                                NeedUpdateScreen(
                                    onExitClick = { finish() },
                                    onUpdateClick = ::moveToPlayStoreForUpdate,
                                )
                            }
                        }
                    }
                }
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

    private fun createSignInIntent() {
        val providers =
            arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
            )

        val signInIntent =
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            initialViewModel.fetchStartDestination()
        } else {
            Log.d("auth", "로그인 실패 ${response?.error?.message}")
        }
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

enum class BackgroundState {
    GRADIENT,
    DEFAULT,
}
