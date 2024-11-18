package com.teamhy2.hongikyeolgong2.notification

import androidx.annotation.StringRes

enum class PushText(
    @StringRes val id: Int,
) {
    THIRTY_MINUTES(R.string.notification_content_thirty_minutes_remain),
    TEN_MINUTES(R.string.notification_content_ten_minutes_remain),
    ZERO_MINUTES(R.string.notification_content_zero_minutes_remain),
}
