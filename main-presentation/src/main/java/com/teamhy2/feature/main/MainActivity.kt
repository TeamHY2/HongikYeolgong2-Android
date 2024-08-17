package com.teamhy2.feature.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.teamhy2.designsystem.ui.theme.HY2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val signInLauncher =
        registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            this.onSignInResult(res)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = Firebase.auth.currentUser
        Log.d("auth", "onCreate: ${auth?.email}")

        enableEdgeToEdge()
        setContent {
            HY2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        AppNavHost(navController = rememberNavController())
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
            // 구글 로그인 성공시 로직
            // val user = FirebaseAuth.getInstance().currentUser
        } else {
            Log.d("auth", "로그인 실패 ${response?.error?.message}")
        }
    }
}
