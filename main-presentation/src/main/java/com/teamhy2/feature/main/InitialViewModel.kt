package com.teamhy2.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.benenfeldt.remote.token.JwtManager
import com.benenfeldt.remote.token.TokenRole
import com.benenfeldt.remote.token.TokenValidator
import com.google.firebase.firestore.FirebaseFirestore
import com.teamhy2.feature.home.navigation.Home
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import com.teamhy2.onboarding.navigation.Onboarding
import com.teamhy2.onboarding.navigation.SignUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InitialViewModel
    @Inject
    constructor(
        private val webViewRepository: WebViewRepository,
        private val jwtManager: JwtManager,
        private val tokenValidator: TokenValidator,
    ) : ViewModel(), ContainerHost<InitialState, InitialSideEffect> {
        override val container: Container<InitialState, InitialSideEffect> =
            container(InitialState.Loading)

        init {
            initInitialState()
        }

        private fun initInitialState() =
            intent {
                runCatching {
                    coroutineScope {
                        val deferredStartDestination: Deferred<String> =
                            async { getStartDestination() }
                        val deferredUrls: Deferred<Map<String, String>> =
                            async { webViewRepository.fetchFirebaseUrls() }

                        deferredStartDestination.await() to deferredUrls.await()
                    }
                }
                    .onSuccess { (startDestination, urls) ->
                        reduce {
                            InitialState.Success(
                                startDestination = startDestination,
                                urls = urls.toImmutableMap(),
                            )
                        }
                    }
                    .onFailure {
                        postSideEffect(InitialSideEffect.ShowError(it))
                    }
            }

        private suspend fun getStartDestination(): String {
            jwtManager.getAccessJwt() ?: return resetToken()

            val isValidToken = tokenValidator.validate().getOrNull() ?: return resetToken()

            return when (isValidToken.role) {
                TokenRole.USER -> Home.NAVIGATION_ROUTE
                TokenRole.GUEST -> SignUp.ROUTE
                TokenRole.ADMIN -> resetToken()
            }
        }

        private suspend fun resetToken(): String {
            jwtManager.clearAllTokens()
            return Onboarding.ROUTE
        }

        fun getMinVersion(currentVersion: Long) =
            intent {
                val firebaseStore = FirebaseFirestore.getInstance()

                runCatching {
                    firebaseStore.collection(COLLECTION_APP_VERSION).document(DOCUMENT_ANDROID).get()
                        .await()
                }
                    .onSuccess {
                        val minVersion = it.get("minVersion")
                        if (minVersion.toString().toLong() > currentVersion) {
                            reduce { InitialState.NeedUpdate }
                        }
                    }
                    .onFailure {
                        Log.d("FireStore", "getMinVersion: ${it.message}")
                    }
            }

        companion object {
            private const val COLLECTION_APP_VERSION = "AppVersion"
            private const val DOCUMENT_ANDROID = "Android"
        }
    }
