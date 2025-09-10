package com.teamhy2.main.domain.util

import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * "HH:mm:ss" 형식의 시간 문자열을 총 초 단위(Long)로 변환합니다.
 *
 * 이 함수는 시간, 분, 초에 24시간제나 60분/초 제한을 두지 않으며,
 * 음수나 각 단위가 두자리 수가 맞지 않는 숫자도 처리합니다. (예: "99:1:5", "-1:0:0")
 *
 * @param timeString 콜론(:)으로 구분된 "시:분:초" 형식의 문자열.
 * @return 계산된 총 초(Long 타입).
 * @throws NumberFormatException 시간, 분, 초 중 숫자로 변환할 수 없는 값이 포함된 경우 발생합니다.
 * @throws IndexOutOfBoundsException "HH:mm:ss" 형식처럼 ':'으로 시간, 분, 초를 구분하지 않을 경우 발생합니다.
 */
fun parseTimeToSeconds(timeString: String): Long {
    val (hour, minute, second) = timeString.split(':').map<String, Long> { it.toLong() }
    return TimeUnit.HOURS.toSeconds(hour) + TimeUnit.MINUTES.toSeconds(minute) + second
}

private const val SECONDS_PER_MINUTE = 60
private const val SECONDS_PER_HOUR = 3600

/**
 * 총 초(Long)를 "HH:mm:ss" 형식의 시간 문자열로 변환합니다.
 *
 * 이 함수는 입력된 총 초를 시, 분, 초로 계산하여 항상 각 단위를 두 자리 숫자로
 * 표시합니다. (예: 1L -> "00:00:01")
 * 24시간을 초과하는 값도 그대로 시간으로 변환합니다. (예: 86400L -> "24:00:00")
 *
 * @param totalSeconds 변환할 총 초 단위의 값. 0 이상이어야 합니다.
 * @return "HH:mm:ss" 형식으로 포맷된 시간 문자열.
 * @throws IllegalArgumentException `totalSeconds`가 음수일 경우 발생합니다.
 */
fun formatSecondsToTime(totalSeconds: Long): String {
    require(totalSeconds >= 0) { "totalSeconds는 0이상이어야 합니다. 잘못된 값: $totalSeconds" }

    val hours = totalSeconds / SECONDS_PER_HOUR
    val minutes = (totalSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
    val seconds = totalSeconds % SECONDS_PER_MINUTE
    return String.format(Locale.KOREA, "%02d:%02d:%02d", hours, minutes, seconds)
}
