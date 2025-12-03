package com.teamhy2.feature.main

import android.content.Context
import android.content.Intent
import com.benenfeldt.remote.token.AuthCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainAuthCallback @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthCallback {
    override fun onAuthRequired() {
        val intent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        context.startActivity(intent)
    }
}
