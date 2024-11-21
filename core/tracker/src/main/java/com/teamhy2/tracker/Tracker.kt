package com.teamhy2.tracker

interface Tracker {
    fun trackEvent(
        eventName: String,
        properties: Map<String, Any?> = emptyMap(),
    )
}
