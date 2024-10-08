package com.teamhy2.feature.main

typealias UrlName = String
typealias UrlValue = String

interface InitialUiState {
    data object Loading : InitialUiState

    data class Success(
        val startDestination: String = "",
        val urls: Map<UrlName, UrlValue> = emptyMap(),
    ) : InitialUiState
}
