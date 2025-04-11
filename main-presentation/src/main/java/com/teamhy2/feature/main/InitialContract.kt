package com.teamhy2.feature.main

typealias UrlName = String
typealias UrlValue = String

sealed interface InitialState {
    data object Loading : InitialState

    data object NeedUpdate : InitialState

    data class Success(
        val startDestination: String,
        val urls: Map<UrlName, UrlValue> = emptyMap(),
    ) : InitialState
}

sealed interface InitialSideEffect {
    data class ShowError(val throwable: Throwable) : InitialSideEffect
}
