package com.teamhy2.feature.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.feature.main.navigation.Main
import com.teamhy2.hongikyeolgong2.main.presentation.R
import com.teamhy2.onboarding.OnboardingViewModel
import com.teamhy2.onboarding.navigation.Onboarding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val DEFAULT_BACKGROUND_OPACITY = 0.7f

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val signInLauncher =
        registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            this.onSignInResult(res)
        }

    private val onboardingViewModel: OnboardingViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = Firebase.auth.currentUser
        Log.d("auth", "onCreate: ${auth?.email}")

        onboardingViewModel.checkUserExists(auth?.uid)

        var startDestination: String
        if (auth == null) {
            startDestination = Onboarding.ROUTE
        } else {
            startDestination = Main.ROUTE
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    onboardingViewModel.userExists.collect { userExists ->
                        startDestination =
                            if (userExists) {
                                Main.ROUTE
                            } else {
                                Onboarding.ROUTE
                            }
                    }
                }
            }
        }
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
                    val notificationHandler = onboardingViewModel.notificationHandler

                    LaunchedEffect(key1 = true) {
                        if (!postNotificationPermission.status.isGranted) {
                            postNotificationPermission.launchPermissionRequest()
                        }
                    }

                    var backgroundState by remember {
                        mutableStateOf(BackgroundState.DEFAULT)
                    }

                    Column(
                        when (backgroundState) {
                            BackgroundState.GRADIENT ->
                                Modifier.paint(
                                    painter = painterResource(id = R.drawable.backgroud),
                                    contentScale = ContentScale.FillBounds,
                                    alpha = DEFAULT_BACKGROUND_OPACITY,
                                )

                            BackgroundState.DEFAULT -> Modifier.background(BackgroundBlack)
                        }
                            .padding(innerPadding),
                    ) {
                        HY2NavHost(
                            navController = rememberNavController(),
                            urls = onboardingViewModel.urls,
                            googleSignIn = ::createSignInIntent,
                            startDestination = startDestination,
                            onSendNotification = { pushText ->
                                notificationHandler.showSimpleNotification(pushText)
                            },
                            onLogoutOrWithdrawComplete = {
                                restartMainActivity()
                            },
                            onBackgroundChanged = { background ->
                                backgroundState = background
                            },
                        )
                    }
                }
            }
        }
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
            Log.d("auth", "로그인 성공 ${Firebase.auth.currentUser?.email}")
            val auth = Firebase.auth.currentUser
            if (auth != null) {
                onboardingViewModel.checkUserExists(auth.uid)
            }
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
}

enum class BackgroundState {
    GRADIENT,
    DEFAULT,
}
