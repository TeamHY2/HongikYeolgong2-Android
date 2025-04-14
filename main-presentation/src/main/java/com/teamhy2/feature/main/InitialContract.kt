package com.teamhy2.feature.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableMap

typealias UrlName = String
typealias UrlValue = String

@Stable
sealed interface InitialState {
    @Immutable
    data object Loading : InitialState

    @Immutable
    data object NeedUpdate : InitialState

    @Immutable
    data class Success(
        val startDestination: String,
        val urls: ImmutableMap<UrlName, UrlValue>,
    ) : InitialState
}

sealed interface InitialSideEffect {
    data class ShowError(val throwable: Throwable) : InitialSideEffect
}
